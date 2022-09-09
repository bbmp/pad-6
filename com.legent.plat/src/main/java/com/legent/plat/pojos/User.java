package com.legent.plat.pojos;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.legent.pojos.AbsKeyPojo;
import com.legent.utils.graphic.BitmapUtils;
import com.legent.utils.security.MD5Utils;

import java.util.HashMap;
import java.util.Map;

public class User extends AbsKeyPojo<Long> implements Parcelable {

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    /**
     * 用户ID
     */
    @JsonProperty("id")
    public long id;
    /**
     * 用户昵称
     */
    @JsonProperty("nickname")
    public String name;
    /**
     * 性别
     */
    @JsonProperty("gender")
    public boolean gender;
    /**
     * email
     */
    @JsonProperty("email")
    public String email;
    /**
     * 手机号码
     */
    @JsonProperty("phone")
    public String phone;
    /**
     * 用户口令(已加密)
     */
    @JsonProperty("password")
    public String password;
    /**
     * 用户头像(url形式)
     */
    @JsonProperty("figureUrl")
    public String figureUrl;

    /**
     * 绑定的第三方平台账户
     */
    @JsonProperty("thirdBind")
    public Map<Integer, User3rd> mapThirdParty;
    public String TGT, ST;
    public int loginPlatId;

    public User() {
    }

    private User(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.gender = in.readByte() != 0;
        this.email = in.readString();
        this.phone = in.readString();
        this.password = in.readString();
        this.figureUrl = in.readString();
        this.mapThirdParty = in.readHashMap(HashMap.class.getClassLoader());
        this.TGT = in.readString();
        this.ST = in.readString();
        this.loginPlatId = in.readInt();
    }

    static public String figure2String(Bitmap bmp) {
        String str = BitmapUtils.toBase64(bmp);
        return str;
    }

    static public String encryptPassword(String rawPwd) {
        return MD5Utils.Md5(rawPwd);
    }

    static public String getEmptyPwd() {
        return MD5Utils.Md5("");
    }

    @Override
    public Long getID() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getAccount() {
        return !Strings.isNullOrEmpty(phone) ? phone : email;
    }

    public User3rd getUser3rd(int platId) {
        if (mapThirdParty != null && mapThirdParty.containsKey(platId)) {
            User3rd u = mapThirdParty.get(platId);
            return u;
        } else
            return null;

    }

    public void bind3rd(int platId, User3rd user) {
        if (mapThirdParty == null) {
            mapThirdParty = Maps.newHashMap();
        }
        mapThirdParty.put(platId, user);
    }

    public void unbind3rd(int platId) {
        if (mapThirdParty != null) {
            mapThirdParty.remove(platId);
        }
    }

    public boolean hasPassword() {
        if (password == null)
            return false;

        String emptyPwd = getEmptyPwd();
        boolean hasPwd = !Objects.equal(password, emptyPwd);
        return hasPwd;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeByte(gender ? (byte) 1 : (byte) 0);
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.password);
        dest.writeString(this.figureUrl);
        dest.writeMap(this.mapThirdParty);
        dest.writeString(this.TGT);
        dest.writeString(this.ST);
        dest.writeInt(this.loginPlatId);
    }
}
