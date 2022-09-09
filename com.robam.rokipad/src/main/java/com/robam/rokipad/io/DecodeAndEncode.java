package com.robam.rokipad.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



public class DecodeAndEncode {
    final public static byte HEAD[] = {(byte) 0xFE,(byte)0x5C};
    final public static byte OPTIONAL_ENCRYPT_BIT = 0x01;
    final public static byte OPTIONAL_CRC_BIT = 0x02;
    final public static byte OPTIONAL_BROADCAST_DATALINK_BIT = 0x04;
    final public static byte OPTIONAL_CHECKSUM_BIT = 0x08;

    final public static byte READ_SYNC_ST0 = 0;
    final public static byte READ_SYNC_ST1 = 1;
    final public static byte READ_OPTIONAL = 2;
    final public static byte READ_MORE_LEN_BYTE_ST = 3;
    final public static byte READ_DATA_ST = 4;

    private int mRecoderId = 0;

    private List<DecodeCell> mList = new LinkedList<DecodeCell>();
    private byte mStatus = READ_SYNC_ST0;
    private int mDataLenBytes;
    private byte mOptional;
    private int mDataSize;
    private int mCur;
    private byte[] mHeadBuff = new byte[8];
    private byte[] mDataBuff;
    private Random mRandom = new Random(10);

    static final private byte[] semCurityEncryptTable =
    {
            (byte)0x56,  (byte)0x80,  (byte)0x42,  (byte)0x84,  (byte)0x45,  (byte)0x87,  (byte)0x47,  (byte)0x86,  (byte)0x44,  (byte)0x85,  (byte)0x82,  (byte)0x46,  (byte)0x43,  (byte)0x83,  (byte)0x41,  (byte)0x81,
            (byte)0x98,  (byte)0x58,  (byte)0x9A,  (byte)0x5C,  (byte)0x9D,  (byte)0x5F,  (byte)0x9F,  (byte)0x5E,  (byte)0x9C,  (byte)0x5D,  (byte)0x5A,  (byte)0x9E,  (byte)0x9B,  (byte)0x5B,  (byte)0x99,  (byte)0x59,
            (byte)0x8C,  (byte)0x4C,  (byte)0x8E,  (byte)0x48,  (byte)0x89,  (byte)0x4B,  (byte)0x8B,  (byte)0x4A,  (byte)0x88,  (byte)0x49,  (byte)0x4E,  (byte)0x8A,  (byte)0x8F,  (byte)0x4F,  (byte)0x8D,  (byte)0x4D,
            (byte)0x54,  (byte)0x40,  (byte)0x94,  (byte)0x90,  (byte)0x51,  (byte)0x93,  (byte)0x53,  (byte)0x92,  (byte)0x50,  (byte)0x91,  (byte)0x96,  (byte)0x52,  (byte)0x57,  (byte)0x97,  (byte)0x55,  (byte)0x95,
            (byte)0x04,  (byte)0xC4,  (byte)0x06,  (byte)0xC0,  (byte)0x01,  (byte)0xC3,  (byte)0x03,  (byte)0xC2,  (byte)0x00,  (byte)0xC1,  (byte)0xC6,  (byte)0x02,  (byte)0x07,  (byte)0xC7,  (byte)0x05,  (byte)0xC5,
            (byte)0x10,  (byte)0xD0,  (byte)0x12,  (byte)0xD4,  (byte)0x15,  (byte)0xD7,  (byte)0x17,  (byte)0xD6,  (byte)0x14,  (byte)0xD5,  (byte)0xD2,  (byte)0x16,  (byte)0x13,  (byte)0xD3,  (byte)0x11,  (byte)0xD1,
            (byte)0x38,  (byte)0xF8,  (byte)0x3A,  (byte)0xFC,  (byte)0x3D,  (byte)0xFF,  (byte)0x3F,  (byte)0xFE,  (byte)0x3C,  (byte)0xFD,  (byte)0xFA,  (byte)0x3E,  (byte)0x3B,  (byte)0xFB,  (byte)0x39,  (byte)0xF9,
            (byte)0x7C,  (byte)0xBC,  (byte)0x7E,  (byte)0xB8,  (byte)0x79,  (byte)0xBB,  (byte)0x7B,  (byte)0xBA,  (byte)0x78,  (byte)0xB9,  (byte)0xBE,  (byte)0x7A,  (byte)0x7F,  (byte)0xBF,  (byte)0x7D,  (byte)0xBD,
            (byte)0xA4,  (byte)0x64,  (byte)0xA6,  (byte)0x60,  (byte)0xA1,  (byte)0x63,  (byte)0xA3,  (byte)0x62,  (byte)0xA0,  (byte)0x61,  (byte)0x66,  (byte)0xA2,  (byte)0xA7,  (byte)0x67,  (byte)0xA5,  (byte)0x65,
            (byte)0xE0,  (byte)0x20,  (byte)0xE2,  (byte)0x24,  (byte)0xE5,  (byte)0x27,  (byte)0xE7,  (byte)0x26,  (byte)0xE4,  (byte)0x25,  (byte)0x22,  (byte)0xE6,  (byte)0xE3,  (byte)0x23,  (byte)0xE1,  (byte)0x21,
            (byte)0xF4,  (byte)0x34,  (byte)0xF6,  (byte)0x30,  (byte)0xF1,  (byte)0x33,  (byte)0xF3,  (byte)0x32,  (byte)0xF0,  (byte)0x31,  (byte)0x36,  (byte)0xF2,  (byte)0xF7,  (byte)0x37,  (byte)0xF5,  (byte)0x35,
            (byte)0xDC,  (byte)0x1C,  (byte)0xDE,  (byte)0x18,  (byte)0xD9,  (byte)0x1B,  (byte)0xDB,  (byte)0x1A,  (byte)0xD8,  (byte)0x19,  (byte)0x1E,  (byte)0xDA,  (byte)0xDF,  (byte)0x1F,  (byte)0xDD,  (byte)0x1D,
            (byte)0xC8,  (byte)0x08,  (byte)0xCA,  (byte)0x0C,  (byte)0xCD,  (byte)0x0F,  (byte)0xCF,  (byte)0x0E,  (byte)0xCC,  (byte)0x0D,  (byte)0x0A,  (byte)0xCE,  (byte)0xCB,  (byte)0x0B,  (byte)0xC9,  (byte)0x09,
            (byte)0xB0,  (byte)0x70,  (byte)0xB2,  (byte)0x74,  (byte)0xB5,  (byte)0x77,  (byte)0xB7,  (byte)0x76,  (byte)0xB4,  (byte)0x75,  (byte)0x72,  (byte)0xB6,  (byte)0xB3,  (byte)0x73,  (byte)0xB1,  (byte)0x71,
            (byte)0x68,  (byte)0xA8,  (byte)0x6A,  (byte)0xAC,  (byte)0x6D,  (byte)0xAF,  (byte)0x6F,  (byte)0xAE,  (byte)0x6C,  (byte)0xAD,  (byte)0xAA,  (byte)0x6E,  (byte)0x6B,  (byte)0xAB,  (byte)0x69,  (byte)0xA9,
            (byte)0x2C,  (byte)0xEC,  (byte)0x2E,  (byte)0xE8,  (byte)0x29,  (byte)0xEB,  (byte)0x2B,  (byte)0xEA,  (byte)0x28,  (byte)0xE9,  (byte)0xEE,  (byte)0x2A,  (byte)0x2F,  (byte)0xEF,  (byte)0x2D,  (byte)0xED
    };

    static final private byte[] semCurityDecryptTable =
    {
            (byte)0x48, (byte)0x44, (byte)0x4b, (byte)0x46, (byte)0x40, (byte)0x4e, (byte)0x42, (byte)0x4c, (byte)0xc1, (byte)0xcf, (byte)0xca, (byte)0xcd, (byte)0xc3, (byte)0xc9, (byte)0xc7, (byte)0xc5,
            (byte)0x50, (byte)0x5e, (byte)0x52, (byte)0x5c, (byte)0x58, (byte)0x54, (byte)0x5b, (byte)0x56, (byte)0xb3, (byte)0xb9, (byte)0xb7, (byte)0xb5, (byte)0xb1, (byte)0xbf, (byte)0xba, (byte)0xbd,
            (byte)0x91, (byte)0x9f, (byte)0x9a, (byte)0x9d, (byte)0x93, (byte)0x99, (byte)0x97, (byte)0x95, (byte)0xf8, (byte)0xf4, (byte)0xfb, (byte)0xf6, (byte)0xf0, (byte)0xfe, (byte)0xf2, (byte)0xfc,
            (byte)0xa3, (byte)0xa9, (byte)0xa7, (byte)0xa5, (byte)0xa1, (byte)0xaf, (byte)0xaa, (byte)0xad, (byte)0x60, (byte)0x6e, (byte)0x62, (byte)0x6c, (byte)0x68, (byte)0x64, (byte)0x6b, (byte)0x66,
            (byte)0x31, (byte)0x0e, (byte)0x02, (byte)0x0c, (byte)0x08, (byte)0x04, (byte)0x0b, (byte)0x06, (byte)0x23, (byte)0x29, (byte)0x27, (byte)0x25, (byte)0x21, (byte)0x2f, (byte)0x2a, (byte)0x2d,
            (byte)0x38, (byte)0x34, (byte)0x3b, (byte)0x36, (byte)0x30, (byte)0x3e, (byte)0x00, (byte)0x3c, (byte)0x11, (byte)0x1f, (byte)0x1a, (byte)0x1d, (byte)0x13, (byte)0x19, (byte)0x17, (byte)0x15,
            (byte)0x83, (byte)0x89, (byte)0x87, (byte)0x85, (byte)0x81, (byte)0x8f, (byte)0x8a, (byte)0x8d, (byte)0xe0, (byte)0xee, (byte)0xe2, (byte)0xec, (byte)0xe8, (byte)0xe4, (byte)0xeb, (byte)0xe6,
            (byte)0xd1, (byte)0xdf, (byte)0xda, (byte)0xdd, (byte)0xd3, (byte)0xd9, (byte)0xd7, (byte)0xd5, (byte)0x78, (byte)0x74, (byte)0x7b, (byte)0x76, (byte)0x70, (byte)0x7e, (byte)0x72, (byte)0x7c,
            (byte)0x01, (byte)0x0f, (byte)0x0a, (byte)0x0d, (byte)0x03, (byte)0x09, (byte)0x07, (byte)0x05, (byte)0x28, (byte)0x24, (byte)0x2b, (byte)0x26, (byte)0x20, (byte)0x2e, (byte)0x22, (byte)0x2c,
            (byte)0x33, (byte)0x39, (byte)0x37, (byte)0x35, (byte)0x32, (byte)0x3f, (byte)0x3a, (byte)0x3d, (byte)0x10, (byte)0x1e, (byte)0x12, (byte)0x1c, (byte)0x18, (byte)0x14, (byte)0x1b, (byte)0x16,
            (byte)0x88, (byte)0x84, (byte)0x8b, (byte)0x86, (byte)0x80, (byte)0x8e, (byte)0x82, (byte)0x8c, (byte)0xe1, (byte)0xef, (byte)0xea, (byte)0xed, (byte)0xe3, (byte)0xe9, (byte)0xe7, (byte)0xe5,
            (byte)0xd0, (byte)0xde, (byte)0xd2, (byte)0xdc, (byte)0xd8, (byte)0xd4, (byte)0xdb, (byte)0xd6, (byte)0x73, (byte)0x79, (byte)0x77, (byte)0x75, (byte)0x71, (byte)0x7f, (byte)0x7a, (byte)0x7d,
            (byte)0x43, (byte)0x49, (byte)0x47, (byte)0x45, (byte)0x41, (byte)0x4f, (byte)0x4a, (byte)0x4d, (byte)0xc0, (byte)0xce, (byte)0xc2, (byte)0xcc, (byte)0xc8, (byte)0xc4, (byte)0xcb, (byte)0xc6,
            (byte)0x51, (byte)0x5f, (byte)0x5a, (byte)0x5d, (byte)0x53, (byte)0x59, (byte)0x57, (byte)0x55, (byte)0xb8, (byte)0xb4, (byte)0xbb, (byte)0xb6, (byte)0xb0, (byte)0xbe, (byte)0xb2, (byte)0xbc,
            (byte)0x90, (byte)0x9e, (byte)0x92, (byte)0x9c, (byte)0x98, (byte)0x94, (byte)0x9b, (byte)0x96, (byte)0xf3, (byte)0xf9, (byte)0xf7, (byte)0xf5, (byte)0xf1, (byte)0xff, (byte)0xfa, (byte)0xfd,
            (byte)0xa8, (byte)0xa4, (byte)0xab, (byte)0xa6, (byte)0xa0, (byte)0xae, (byte)0xa2, (byte)0xac, (byte)0x61, (byte)0x6f, (byte)0x6a, (byte)0x6d, (byte)0x63, (byte)0x69, (byte)0x67, (byte)0x65
    };

    static final private byte[] crcLTable = {
            (byte)0x00, (byte)0xC0, (byte)0xC1, (byte)0x01, (byte)0xC3, (byte)0x03, (byte)0x02, (byte)0xC2, (byte)0xC6, (byte)0x06, (byte)0x07, (byte)0xC7,	(byte)0x05, (byte)0xC5, (byte)0xC4, (byte)0x04,
            (byte)0xCC, (byte)0x0C, (byte)0x0D, (byte)0xCD, (byte)0x0F, (byte)0xCF, (byte)0xCE, (byte)0x0E,	(byte)0x0A, (byte)0xCA, (byte)0xCB, (byte)0x0B, (byte)0xC9, (byte)0x09, (byte)0x08, (byte)0xC8,
            (byte)0xD8, (byte)0x18, (byte)0x19, (byte)0xD9,	(byte)0x1B, (byte)0xDB, (byte)0xDA, (byte)0x1A, (byte)0x1E, (byte)0xDE, (byte)0xDF, (byte)0x1F, (byte)0xDD, (byte)0x1D, (byte)0x1C, (byte)0xDC,
            (byte)0x14, (byte)0xD4, (byte)0xD5, (byte)0x15, (byte)0xD7, (byte)0x17, (byte)0x16, (byte)0xD6, (byte)0xD2, (byte)0x12, (byte)0x13, (byte)0xD3,	(byte)0x11, (byte)0xD1, (byte)0xD0, (byte)0x10,
            (byte)0xF0, (byte)0x30, (byte)0x31, (byte)0xF1, (byte)0x33, (byte)0xF3, (byte)0xF2, (byte)0x32,	(byte)0x36, (byte)0xF6, (byte)0xF7, (byte)0x37, (byte)0xF5, (byte)0x35, (byte)0x34, (byte)0xF4,
            (byte)0x3C, (byte)0xFC, (byte)0xFD, (byte)0x3D,	(byte)0xFF, (byte)0x3F, (byte)0x3E, (byte)0xFE, (byte)0xFA, (byte)0x3A, (byte)0x3B, (byte)0xFB, (byte)0x39, (byte)0xF9, (byte)0xF8, (byte)0x38,
            (byte)0x28, (byte)0xE8, (byte)0xE9, (byte)0x29, (byte)0xEB, (byte)0x2B, (byte)0x2A, (byte)0xEA, (byte)0xEE, (byte)0x2E, (byte)0x2F, (byte)0xEF,	(byte)0x2D, (byte)0xED, (byte)0xEC, (byte)0x2C,
            (byte)0xE4, (byte)0x24, (byte)0x25, (byte)0xE5, (byte)0x27, (byte)0xE7, (byte)0xE6, (byte)0x26,	(byte)0x22, (byte)0xE2, (byte)0xE3, (byte)0x23, (byte)0xE1, (byte)0x21, (byte)0x20, (byte)0xE0,
            (byte)0xA0, (byte)0x60, (byte)0x61, (byte)0xA1,	(byte)0x63, (byte)0xA3, (byte)0xA2, (byte)0x62, (byte)0x66, (byte)0xA6, (byte)0xA7, (byte)0x67, (byte)0xA5, (byte)0x65, (byte)0x64, (byte)0xA4,
            (byte)0x6C, (byte)0xAC, (byte)0xAD, (byte)0x6D, (byte)0xAF, (byte)0x6F, (byte)0x6E, (byte)0xAE, (byte)0xAA, (byte)0x6A, (byte)0x6B, (byte)0xAB,	(byte)0x69, (byte)0xA9, (byte)0xA8, (byte)0x68,
            (byte)0x78, (byte)0xB8, (byte)0xB9, (byte)0x79, (byte)0xBB, (byte)0x7B, (byte)0x7A, (byte)0xBA,	(byte)0xBE, (byte)0x7E, (byte)0x7F, (byte)0xBF, (byte)0x7D, (byte)0xBD, (byte)0xBC, (byte)0x7C,
            (byte)0xB4, (byte)0x74, (byte)0x75, (byte)0xB5,	(byte)0x77, (byte)0xB7, (byte)0xB6, (byte)0x76, (byte)0x72, (byte)0xB2, (byte)0xB3, (byte)0x73, (byte)0xB1, (byte)0x71, (byte)0x70, (byte)0xB0,
            (byte)0x50, (byte)0x90, (byte)0x91, (byte)0x51, (byte)0x93, (byte)0x53, (byte)0x52, (byte)0x92, (byte)0x96, (byte)0x56, (byte)0x57, (byte)0x97,	(byte)0x55, (byte)0x95, (byte)0x94, (byte)0x54,
            (byte)0x9C, (byte)0x5C, (byte)0x5D, (byte)0x9D, (byte)0x5F, (byte)0x9F, (byte)0x9E, (byte)0x5E,	(byte)0x5A, (byte)0x9A, (byte)0x9B, (byte)0x5B, (byte)0x99, (byte)0x59, (byte)0x58, (byte)0x98,
            (byte)0x88, (byte)0x48, (byte)0x49, (byte)0x89,	(byte)0x4B, (byte)0x8B, (byte)0x8A, (byte)0x4A, (byte)0x4E, (byte)0x8E, (byte)0x8F, (byte)0x4F, (byte)0x8D, (byte)0x4D, (byte)0x4C, (byte)0x8C,
            (byte)0x44, (byte)0x84, (byte)0x85, (byte)0x45, (byte)0x87, (byte)0x47, (byte)0x46, (byte)0x86, (byte)0x82, (byte)0x42, (byte)0x43, (byte)0x83,	(byte)0x41, (byte)0x81, (byte)0x80, (byte)0x40
    };

    static final private byte[] crcHTable = {
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,	(byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,
            (byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41, (byte)0x01, (byte)0xC0, (byte)0x80, (byte)0x41,	(byte)0x00, (byte)0xC1, (byte)0x81, (byte)0x40
    };

    private byte[] calCRC16(byte buff[])
    {
        byte crcH = (byte)0xFF;
        byte crcL = (byte)0xFF;
        int index;
        for(int i = 0;i < buff.length ;i++)
        {
            index = (crcL ^ buff[i]) & 0xff;
            crcL = (byte) (crcH ^ crcHTable[index]);
            crcH = crcLTable[index];
        }
        return new byte[] {crcL, crcH};

    }

    private byte[] calCRC16(byte[]buff, int offset, int len)
    {
        byte crcH = (byte)0xFF;
        byte crcL = (byte)0xFF;
        int index;
        for(int i = offset; i < len + offset && i < buff.length;i++)
        {
            index = (crcL ^ buff[i]) & 0xff;
            crcL = (byte) (crcH ^ crcHTable[index]);
            crcH = crcLTable[index];
        }
        return new byte[] {crcL, crcH};
    }

    private byte [] encrypt(byte[] org, byte random) {
        byte[] output = new byte[org.length +1];
        byte tmp;
        for (int i = 0; i < org.length; i++) {
            tmp = (byte) (org[i] ^ random);
            output[i + 1] = semCurityEncryptTable[tmp & 0xff];
        }
        output[0] = semCurityEncryptTable[random & 0xff];
        return output;
    }

    private byte [] encrypt(byte[] org, int offset, byte random) {
        byte[] output = new byte[org.length + 1 - offset];
        byte tmp;
        for (int i = 0; i < org.length - offset; i++) {
            tmp = (byte) (org[i + offset] ^ random);
            output[i + 1] = semCurityEncryptTable[tmp & 0xff];
        }
        output[0] = semCurityEncryptTable[random & 0xff];
        return output;
    }

    private byte[] decrypt(byte[]org) {
        byte[] output = new byte[org.length];
        byte tmp;
        byte random = semCurityDecryptTable[org[0] & 0xff];
        for (int i = 1; i < org.length; i++) {
            tmp = semCurityDecryptTable[org[i] & 0xff];
            output[i] = (byte)(tmp ^ random);
        }
        output[0] = random;
        return output;
    }

    private byte [] decrypt(byte[] org, int offset, int len) {
        if ((offset >= org.length) || (offset + len > org.length)) {
            return null;
        }

        byte[] output = new byte[len];
        byte tmp;
        byte random = semCurityDecryptTable[org[offset] & 0xff];
        for (int i = 1; i < len; i++) {
            tmp = semCurityDecryptTable[org[i+offset] & 0xff];
            output[i] = (byte)(tmp ^ random);
        }
        output[0] = random;
        return output;
    }

    public void decode(byte[] arr, int offset, int len){
        for (int i = offset; i < offset + len && i < arr.length; i++){
            switch (mStatus){
                case READ_SYNC_ST0:
                    if (arr[i] == HEAD[0]){
                        mStatus = READ_SYNC_ST1;
                        mCur = 0;
                        mHeadBuff[mCur++] = arr[i];
                    }
                    break;
                case READ_SYNC_ST1:
                    if (arr[i] == HEAD[1]){
                        mStatus = READ_OPTIONAL;
                        mHeadBuff[mCur++] = arr[i];
                    }else{
                        mStatus = READ_SYNC_ST0;
                    }
                    break;
                case  READ_OPTIONAL:
                    mHeadBuff[mCur++] = arr[i];
                    if (arr[i] != (byte)0x02 && arr[i] != (byte)0x03){
                        DecodeCell c = new DecodeCell();
                        c.id = mRecoderId++;
                        c.err = "optional err!";
                        c.date = new Date();
                        c.beforeDecode = new byte[3];
                        System.arraycopy(mHeadBuff, 0, c.beforeDecode, 0, 3);
                        c.afterDecode = null;
                        mList.add(c);

                        i--;
                        mStatus = READ_SYNC_ST0;
                        break;
                    }
                    mStatus = READ_MORE_LEN_BYTE_ST;
                    mDataLenBytes = 0;
                    mDataSize = 0;
                    mOptional = arr[i];
                    break;
                case READ_MORE_LEN_BYTE_ST:
                    mDataSize += ((arr[i] & 0x7f) << (mDataLenBytes * 7));
                    mDataLenBytes = mDataLenBytes + 1;
                    mHeadBuff[mCur++] = arr[i];
                    if (mDataLenBytes >= 4){
                        DecodeCell c = new DecodeCell();
                        c.id = mRecoderId++;
                        c.err = "len="+mDataSize +"error!";
                        c.date = new Date();
                        c.beforeDecode = new byte[mDataLenBytes + 3];
                        System.arraycopy(mHeadBuff, 0, c.beforeDecode, 0, mDataLenBytes+3);
                        c.afterDecode = null;
                        mList.add(c);

                        mStatus = READ_SYNC_ST0;
                        byte[] tmpBuff = new byte[mCur];
                        System.arraycopy(mHeadBuff,2,tmpBuff,0,mCur-2);
                        decode(tmpBuff);
                        tmpBuff = null;
                        break;
                    }

                    if (0 == (arr[i] & 0x80)){
                        mStatus = READ_DATA_ST;
                        mDataBuff = null;
                        mDataBuff = new byte[mDataSize];
                        mCur = 0;
                    }
                    break;
                case READ_DATA_ST:
                    int copyLen;
                    int remainLen = arr.length - i;
                    if ( remainLen < (mDataSize - mCur)){
                        copyLen = remainLen;
                    }else{
                        copyLen = mDataSize - mCur;
                    }
                    System.arraycopy(arr, i, mDataBuff, mCur, copyLen);
                    mCur += copyLen;
                    i += copyLen -1;
                    if (mCur >= mDataSize){
                        byte[] tmp = null;
                        byte[] crc = null;
                        if ((mOptional & OPTIONAL_ENCRYPT_BIT) == 0){
                            tmp = mDataBuff;
                            crc = calCRC16(mDataBuff, 0, mDataSize - 2);
                        }else{
                            tmp = decrypt(mDataBuff, 0, mDataSize);
                            crc = calCRC16(tmp, 1, tmp.length - 3);
                        }

                        if (crc[0] ==tmp[tmp.length - 2]  && crc[1] == tmp[tmp.length -1]){
                            DecodeCell c = new  DecodeCell();
                            c.id = mRecoderId++;
                            c.err = null;
                            c.date = new Date();
                            c.beforeDecode = new byte[mDataLenBytes + 3 + mDataSize];
                            System.arraycopy(mHeadBuff, 0, c.beforeDecode, 0, mDataLenBytes+3);
                            System.arraycopy(mDataBuff, 0, c.beforeDecode, mDataLenBytes+3, mDataSize);
                            c.afterDecode = tmp;
                            mList.add(c);
                            mStatus = READ_SYNC_ST0;
                        }else{
                            DecodeCell c = new DecodeCell();
                            c.id = mRecoderId++;
                            c.err = "crc error!";
                            c.date = new Date();
                            c.beforeDecode = new byte[mDataLenBytes + 3 + mDataSize];
                            System.arraycopy(mHeadBuff, 0, c.beforeDecode, 0, mDataLenBytes+3);
                            System.arraycopy(mDataBuff, 0, c.beforeDecode, mDataLenBytes+3, mDataSize);
                            c.afterDecode = null;
                            mList.add(c);

                            mStatus = READ_SYNC_ST0;
                            byte[] tmpBuff = new byte[mDataLenBytes + 1 + mDataSize];
                            System.arraycopy(mHeadBuff, 2, tmpBuff, 0, mDataLenBytes+1);
                            System.arraycopy(mDataBuff, 0, tmpBuff, mDataLenBytes+1, mDataSize);
                            decode(tmpBuff);
                            tmpBuff = null;
                        }
                    }
                    break;
            }
        }
    }

    private byte[] encodeDataLen(int len) throws Exception{
        if (len == 0 || len > 268435450){
            throw new Exception("len err");
        }

        int lenBytes = 0;
        byte[] sizeEncode = new byte[4];
        while (len != 0){
            sizeEncode[lenBytes] = (byte)(len & 0x7f);
            sizeEncode[lenBytes] = ((len >>= 7) > 0)? (sizeEncode[lenBytes] |= 0x80):(sizeEncode[lenBytes]);
            lenBytes++;
        }
        byte[] retBytes = new byte[lenBytes];
        System.arraycopy(sizeEncode,0,retBytes,0,lenBytes);
        return retBytes;
    }

    public byte[] encode(byte[] arr, int len, byte option) throws Exception{
        int sizeBytes;
        byte[] encodeBuff, tmp, sizeEncode;

        if (arr == null || arr.length <=0 || len > arr.length){
            throw new Exception("参数非法");
        }

        byte[] crc = calCRC16(arr);
        if ((option & OPTIONAL_ENCRYPT_BIT) == 0){
            sizeEncode = encodeDataLen(len + 2);
            sizeBytes = sizeEncode.length;
            encodeBuff = new byte[len + 5 + sizeBytes];
            System.arraycopy(arr,0,encodeBuff,3+sizeBytes,len);
            System.arraycopy(crc,0,encodeBuff,3+sizeBytes+len,crc.length);
            //tmp = arr;
        }else{
            sizeEncode = encodeDataLen(len + 3);
            sizeBytes = sizeEncode.length;
            tmp = new byte[len+2];
            System.arraycopy(arr,0,tmp,0,len);
            System.arraycopy(crc,0,tmp,len,crc.length);
            tmp = encrypt(tmp,(byte) mRandom.nextInt(255));
            encodeBuff = new byte[tmp.length + 3 + sizeBytes];
            System.arraycopy(tmp,0,encodeBuff,3+sizeBytes,tmp.length);
        }
        encodeBuff[0] = HEAD[0];
        encodeBuff[1] = HEAD[1];
        encodeBuff[2] = option;
        System.arraycopy(sizeEncode,0,encodeBuff,3,sizeBytes);
        return encodeBuff;
    }

    public byte[] encode(byte[] arr, byte option) throws Exception{
        return encode(arr,arr.length,option);
    }

    public byte[] encode(String str, byte option) throws Exception{
        String cell[] = str.split(" ");
        byte val = 0;
        int cur = 0;
        byte[] arr = new byte[cell.length];

        for (int i = 0; i < cell.length; i++){
            if (cell[i].length() > 2){
                throw new Exception("非法数据");
            }
            try{
                val = (byte)(Integer.parseInt(cell[i],16));
            }catch(NumberFormatException e){
                throw new Exception("非法数据");
            }
            arr[cur++] = val;
        }
        return encode(arr,cur,option);
    }

    public List<DecodeCell> getDecodeDate() {
        return mList;
    }

    public void clearDecodeList(){
        if (mList.isEmpty())
            return;

        for (DecodeCell c : mList) {
            c.id = 0;
            c.date = null;
            c.err = null;
            c.beforeDecode = null;
            c.afterDecode = null;
        }
        mList.clear();
    }

    public void decode(byte[] arr){
        decode(arr,0,arr.length);
    }

    public void decode(String str){
        int cur = 0;
        byte[] arr = new byte[1024];
        if (str == null || str.equals(null)){
            return ;
        }

        String[] code = str.split(" ");
        for (int i = 0; i < code.length; i++){
            byte val = 0;

            if (code[i].length() < 2){
                continue;
            }

            if (code[i].length() > 2){
                String head = code[i].substring(0,2);
                String end = code[i].substring(code[i].length()-3, code[i].length()-1);
                try{
                    val = (byte)(Integer.parseInt(head,16));
                }catch(NumberFormatException e){
                    ;
                }
                arr[cur++] = val;

                try{
                    val = (byte)(Integer.parseInt(end,16));
                }catch(NumberFormatException e){
                    ;
                }
                arr[cur++] = val;
                continue;
            }

            try {
                val = (byte)(Integer.parseInt(code[i],16));
            } catch (NumberFormatException e) {
                continue;
            }
            arr[cur++] = val;
            if (cur == arr.length){
                decode(arr,0,cur);
                cur = 0;
            }
        }
        decode(arr,0,cur);
    }

    public void decode(InputStream in) {
        byte[] buffer = new byte[1024];
        int len = -1;
        try {
            while ((len = in.read(buffer)) != -1) {
                decode(buffer, 0, len);
            }
        } catch (IOException e) {
            ;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                ;
            }
        }
    }

    public void decode(File file){
        BufferedReader reader = null;
        try{
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), Charset.forName("utf-8")));
            String line;
            while ((line = reader.readLine()) != null){
                decode(line);
            }
        }catch (Exception e){
            throw new RuntimeException("file err");
        }
        finally {
            try {
                if (reader != null){
                    reader.close();
                }
            }catch (IOException e){
            }
        }
    }


}
