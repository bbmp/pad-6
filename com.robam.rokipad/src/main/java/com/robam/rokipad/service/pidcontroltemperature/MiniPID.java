package com.robam.rokipad.service.pidcontroltemperature;

/**
 * Small, easy to use PID implementation with advanced controller capability.<br>
 * Minimal usage:<br>
 * MiniPID pid = new MiniPID(p,i,d); <br>
 * ...looping code...{ <br>
 * output= pid.getOutput(sensorvalue,target); <br>
 * }
 */
public class MiniPID {
    //**********************************
    // Class private variables
    //**********************************

    private float P = 0;  //增益
    private float I = 0;  //积分
    private float D = 0;  //微分
    private float F = 0;  //

    private float maxIOutput = 0; //最大输出
    private float maxError = 0;   //最大误差
    private float errorSum = 0;   //累计误差

    private float maxOutput = 0;  //
    private float minOutput = 0;

    private float setpoint = 0;//设置量 设置灶具档位

    private float lastActualTemperature = 0;//上一次实际温度

    private boolean firstRun = true;
    private boolean reversed = false;

    private float outputRampRate = 0;
    private float lastOutput = 0; //上一次输出温度

    private float outputFilter = 0;

    private float setpointRange = 0;//设置值范围

    //**********************************
    // Constructor functions
    //**********************************

    /**
     * Create a MiniPID class object.
     * See setP, setI, setD methods for more detailed parameters.
     *
     * @param p Proportional gain. Large if large difference between setpoint and target.
     * @param i Integral gain.  Becomes large if setpoint cannot reach target quickly.
     * @param d Derivative gain. Responds quickly to large changes in error. Small values prevents P and I terms from causing overshoot.
     */
    public MiniPID(float p, float i, float d) {
        P = p;
        I = i;
        D = d;
        checkSigns();
    }

    /**
     * Create a MiniPID class object.
     * See setP, setI, setD, setF methods for more detailed parameters.
     *
     * @param p 比例增益。如果设定点和目标之间的差异很大，则为大。
     * @param i 积分增益。如果设定点不能快速到达目标，则变大。
     * @param d 微分收益。快速响应错误中的大变化。较小的值可防止P和I项引起超调。
     * @param f 前馈增益。开环“最佳猜测”的输出应该是。只有当设定点代表一个速率时才有用。.
     */
    public MiniPID(float p, float i, float d, float f) {
        P = p;
        I = i;
        D = d;
        F = f;
        checkSigns();
    }

    //**********************************
    // Configuration functions
    //**********************************

    /**
     * Configure the Proportional gain parameter. <br>
     * This responds quickly to changes in setpoint, and provides most of the initial driving force
     * to make corrections. <br>
     * Some systems can be used with only a P gain, and many can be operated with only PI.<br>
     * For position based controllers, this is the first parameter to tune, with I second. <br>
     * For rate controlled systems, this is often the second after F.
     *
     * @param p Proportional gain. Affects output according to <b>output+=P*(setpoint-current_value)</b>
     */
    public void setP(float p) {
        P = p;
        checkSigns();
    }

    /**
     * Changes the I parameter <br>
     * This is used for overcoming disturbances, and ensuring that the controller always gets to the control mode.
     * Typically tuned second for "Position" based modes, and third for "Rate" or continuous based modes. <br>
     * Affects output through <b>output+=previous_errors*Igain ;previous_errors+=current_error</b>
     *
     * @param i New gain value for the Integral term
     * @see {@link #setMaxIOutput(float) setMaxIOutput} for how to restrict
     */
    public void setI(float i) {
        if (I != 0) {
            errorSum = errorSum * I / i;
        }
        if (maxIOutput != 0) {
            maxError = maxIOutput / i;
        }
        I = i;
        checkSigns();
        // Implementation note:
        // This Scales the accumulated error to avoid output errors.
        // As an example doubling the I term cuts the accumulated error in half, which results in the
        // output change due to the I term constant during the transition.
    }

    /**
     * Changes the D parameter <br>
     * This has two primary effects:
     * <list>
     * <li> Adds a "startup kick" and speeds up system response during setpoint changes
     * <li> Adds "drag" and slows the system when moving toward the target
     * </list>
     * A small D value can be useful for both improving response times, and preventing overshoot.
     * However, in many systems a large D value will cause significant instability, particularly
     * for large setpoint changes.
     * <br>
     * Affects output through <b>output += -D*(current_input_value - last_input_value)</b>
     *
     * @param d New gain value for the Derivative term
     */
    public void setD(float d) {
        D = d;
        checkSigns();
    }

    /**
     * Configure the FeedForward parameter. <br>
     * This is excellent for velocity, rate, and other  continuous control modes where you can
     * expect a rough output value based solely on the setpoint.<br>
     * Should not be used in "position" based control modes.<br>
     * Affects output according to <b>output+=F*Setpoint</b>. Note, that a F-only system is actually open loop.
     *
     * @param f Feed forward gain.
     */
    public void setF(float f) {
        F = f;
        checkSigns();
    }

    /**
     * Configure the PID object.
     * See setP, setI, setD methods for more detailed parameters.
     *
     * @param p Proportional gain. Large if large difference between setpoint and target.
     * @param i Integral gain.  Becomes large if setpoint cannot reach target quickly.
     * @param d Derivative gain. Responds quickly to large changes in error. Small values prevents P and I terms from causing overshoot.
     */
    public void setPID(float p, float i, float d) {
        P = p;
        D = d;
        //Note: the I term has additional calculations, so we need to use it's
        //specific method for setting it.
        setI(i);
        checkSigns();
    }

    /**
     * Configure the PID object.
     * See setP, setI, setD, setF methods for more detailed parameters.
     *
     * @param p Proportional gain. Large if large difference between setpoint and target.
     * @param i Integral gain.  Becomes large if setpoint cannot reach target quickly.
     * @param d Derivative gain. Responds quickly to large changes in error. Small values prevents P and I terms from causing overshoot.
     * @param f Feed-forward gain. Open loop "best guess" for the output should be. Only useful if setpoint represents a rate.
     */
    public void setPID(float p, float i, float d, float f) {
        P = p;
        D = d;
        F = f;
        //Note: the I term has additional calculations, so we need to use it's
        //specific method for setting it.
        setI(i);
        checkSigns();
    }

    /**
     * Set the maximum output value contributed by the I component of the system
     * This can be used to prevent large windup issues and make tuning simpler
     *
     * @param maximum. Units are the same as the expected output value
     */
    public void setMaxIOutput(float maximum) {
        // Internally maxError and Izone are similar, but scaled for different purposes.
        // The maxError is generated for simplifying math, since calculations against
        // the max error are far more common than changing the I term or Izone.
        maxIOutput = maximum;
        if (I != 0) {
            maxError = maxIOutput / I;
        }
    }

    /**
     * Specify a maximum output range. <br>
     * When one input is specified, output range is configured to
     * <b>[-output, output]</b>
     *
     * @param output
     */
    public void setOutputLimits(float output) {
        setOutputLimits(-output, output);
    }

    /**
     * Specify a  maximum output.
     * When two inputs specified, output range is configured to
     * <b>[minimum, maximum]</b>
     *
     * @param minimum possible output value
     * @param maximum possible output value
     */
    public void setOutputLimits(float minimum, float maximum) {
        if (maximum < minimum) return;
        maxOutput = maximum;
        minOutput = minimum;

        // Ensure the bounds of the I term are within the bounds of the allowable output swing
        if (maxIOutput == 0 || maxIOutput > (maximum - minimum)) {
            setMaxIOutput(maximum - minimum);
        }
    }

    /**
     * Set the operating direction of the PID controller
     *
     * @param reversed Set true to reverse PID output
     */
    public void setDirection(boolean reversed) {
        this.reversed = reversed;
    }

    //**********************************
    // Primary operating functions
    //**********************************

    /**
     * Configure setpoint for the PID calculations<br>
     * This represents the target for the PID system's, such as a
     * position, velocity, or angle. <br>
     *
     * @param setpoint
     * @see MiniPID#getOutput(actual) <br>
     */
    public void setSetpoint(float setpoint) {
        this.setpoint = setpoint;
    }

    /**
     * Calculate the output value for the current PID cycle.<br>
     *
     * @param actual   The monitored value, typically as a sensor input.
     * @param setpoint The target value for the system
     * @return calculated output value for driving the system
     */
    public float getOutput(float actual, float setpoint) {
        float output;
        float Poutput;
        float Ioutput;
        float Doutput;
        float Foutput;

        this.setpoint = setpoint;

        // Ramp the setpoint used for calculations if user has opted to do so
        if (setpointRange != 0) {
            setpoint = constrain(setpoint, actual - setpointRange, actual + setpointRange);
        }

        // Do the simple parts of the calculations
        float error = setpoint - actual;

        // Calculate F output. Notice, this depends only on the setpoint, and not the error.
        Foutput = F * setpoint;

        // Calculate P term
        Poutput = P * error;

        // If this is our first time running this, we don't actually _have_ a previous input or output.
        // For sensor, sanely assume it was exactly where it is now.
        // For last output, we can assume it's the current time-independent outputs.
        if (firstRun) {
            lastActualTemperature = actual;
            lastOutput = Poutput + Foutput;
            firstRun = false;
        }

        // Calculate D Term
        // Note, this is negative. This actually "slows" the system if it's doing
        // the correct thing, and small values helps prevent output spikes and overshoot
        Doutput = -D * (actual - lastActualTemperature);
        lastActualTemperature = actual;

        // The Iterm is more complex. There's several things to factor in to make it easier to deal with.
        // 1. maxIoutput restricts the amount of output contributed by the Iterm.
        // 2. prevent windup by not increasing errorSum if we're already running against our max Ioutput
        // 3. prevent windup by not increasing errorSum if output is output=maxOutput
        Ioutput = I * errorSum;
        if (maxIOutput != 0) {
            Ioutput = constrain(Ioutput, -maxIOutput, maxIOutput);
        }

        // And, finally, we can just add the terms up
        output = Foutput + Poutput + Ioutput + Doutput;

        // Figure out what we're doing with the error.
        if (minOutput != maxOutput && !bounded(output, minOutput, maxOutput)) {
            errorSum = error;
            // reset the error sum to a sane level
            // Setting to current error ensures a smooth transition when the P term
            // decreases enough for the I term to start acting upon the controller
            // From that point the I term will build up as would be expected
        } else if (outputRampRate != 0 && !bounded(output, lastOutput - outputRampRate, lastOutput + outputRampRate)) {
            errorSum = error;
        } else if (maxIOutput != 0) {
            errorSum = constrain(errorSum + error, -maxError, maxError);
            // In addition to output limiting directly, we also want to prevent I term
            // buildup, so restrict the error directly
        } else {
            errorSum += error;
        }

        // Restrict output to our specified output and ramp limits
        if (outputRampRate != 0) {
            output = constrain(output, lastOutput - outputRampRate, lastOutput + outputRampRate);
        }
        if (minOutput != maxOutput) {
            output = constrain(output, minOutput, maxOutput);
        }
        if (outputFilter != 0) {
            output = lastOutput * outputFilter + output * (1 - outputFilter);
        }

        // Get a test printline with lots of details about the internal
        // calculations. This can be useful for debugging.
        // System.out.printf("Final output %5.2f [ %5.2f, %5.2f , %5.2f  ], eSum %.2f\n",output,Poutput, Ioutput, Doutput,errorSum );
        // System.out.printf("%5.2f\t%5.2f\t%5.2f\t%5.2f\n",output,Poutput, Ioutput, Doutput );

        lastOutput = output;
        return output;
    }

    /**
     * Calculate the output value for the current PID cycle.<br>
     * In no-parameter mode, this uses the last sensor value,
     * and last setpoint value. <br>
     * Not typically useful, and use of parameter modes is suggested. <br>
     *
     * @return calculated output value for driving the system
     */
    public float getOutput() {
        return getOutput(lastActualTemperature, setpoint);
    }

    /**
     * Calculate the output value for the current PID cycle.<br>
     * In one parameter mode, the last configured setpoint will be used.<br>
     *
     * @param actual   The monitored value, typically as a sensor input.
     * @param setpoint The target value for the system
     * @return calculated output value for driving the system
     * @see MiniPID#setSetpoint()
     */
    public float getOutput(float actual) {
        return getOutput(actual, setpoint);
    }

    /**
     * Resets the controller. This erases the I term buildup, and removes
     * D gain on the next loop.<br>
     * This should be used any time the PID is disabled or inactive for extended
     * duration, and the controlled portion of the system may have changed due to
     * external forces.
     */
    public void reset() {
        firstRun = true;
        errorSum = 0;
    }

    /**
     * Set the maximum rate the output can increase per cycle.<br>
     * This can prevent sharp jumps in output when changing setpoints or
     * enabling a PID system, which might cause stress on physical or electrical
     * systems.  <br>
     * Can be very useful for fast-reacting control loops, such as ones
     * with large P or D values and feed-forward systems.
     *
     * @param rate, with units being the same as the output
     */
    public void setOutputRampRate(float rate) {
        outputRampRate = rate;
    }

    /**
     * Set a limit on how far the setpoint can be from the current position
     * <br>Can simplify tuning by helping tuning over a small range applies to a much larger range.
     * <br>This limits the reactivity of P term, and restricts impact of large D term
     * during large setpoint adjustments. Increases lag and I term if range is too small.
     *
     * @param range, with units being the same as the expected sensor range.
     */
    public void setSetpointRange(float range) {
        setpointRange = range;
    }

    /**
     * Set a filter on the output to reduce sharp oscillations. <br>
     * 0.1 is likely a sane starting value. Larger values use historical data
     * more heavily, with low values weigh newer data. 0 will disable, filtering, and use
     * only the most recent value. <br>
     * Increasing the filter strength will P and D oscillations, but force larger I
     * values and increase I term overshoot.<br>
     * Uses an exponential wieghted rolling sum filter, according to a simple <br>
     * <pre>output*(1-strength)*sum(0..n){output*strength^n}</pre> algorithm.
     *
     * @param output valid between [0..1), meaning [current output only.. historical output only)
     */
    public void setOutputFilter(float strength) {
        if (strength == 0 || bounded(strength, 0, 1)) {
            outputFilter = strength;
        }
    }

    //**************************************
    // Helper functions
    //**************************************

    /**
     * Forces a value into a specific range
     *
     * @param value input value
     * @param min   maximum returned value
     * @param max   minimum value in range
     * @return Value if it's within provided range, min or max otherwise
     */
    private float constrain(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

    /**
     * Test if the value is within the min and max, inclusive
     *
     * @param value to test
     * @param min   Minimum value of range
     * @param max   Maximum value of range
     * @return true if value is within range, false otherwise
     */
    private boolean bounded(float value, float min, float max) {
        // Note, this is an inclusive range. This is so tests like
        // `bounded(constrain(0,0,1),0,1)` will return false.
        // This is more helpful for determining edge-case behaviour
        // than <= is.
        return (min < value) && (value < max);
    }

    /**
     * To operate correctly, all PID parameters require the same sign
     * This should align with the {@literal}reversed value
     */
    private void checkSigns() {
        if (reversed) {  // all values should be below zero
            if (P > 0) P *= -1;
            if (I > 0) I *= -1;
            if (D > 0) D *= -1;
            if (F > 0) F *= -1;
        } else {  // all values should be above zero
            if (P < 0) P *= -1;
            if (I < 0) I *= -1;
            if (D < 0) D *= -1;
            if (F < 0) F *= -1;
        }
    }
}