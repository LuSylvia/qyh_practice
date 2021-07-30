package com.example.module_common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.example.module_common.utils.channel.ChannelInfo;
import com.example.module_common.utils.channel.ChannelReader;

import java.io.DataInput;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * 获取渠道信息
 */
public final class ChannelUtils {

    private static final String DEFAULT_CHANNEL = "903965_2";//"911435_1";

    private static final int SHORT_LENGTH = 2;
    private static final byte[] MAGIC = new byte[]{0x21, 0x5a, 0x58, 0x4b, 0x21}; //!ZXK!

    /**
     * 获取渠道全称
     *
     * @return channel
     */
    public static String getChannel(Context context) {
        return getChannelInternal(context);
    }

    /**
     * 获取主渠道
     *
     * @return mani channel
     */
    public static String getMainChannel(Context context) {
        return parseChannel(getChannelInternal(context))[0];
    }

    /**
     * 获取子渠道
     *
     * @return sub channel
     */
    public static String getSubChannel(Context context) {
        return parseChannel(getChannelInternal(context))[1];
    }

    /**
     * 解析渠道数组，当无子渠道时，子渠道则为0
     */
    public static String[] parseChannel(String channel) {
        String[] arr = channel.split("_");
        if (arr.length > 1) {
            return arr;
        }
        return new String[]{arr[0], "0"};
    }

    private static String getChannelInternal(Context context) {
        String channel;
        File apk = new File(context.getPackageCodePath());
        ChannelInfo info = ChannelReader.get(apk);
        if (info != null && !TextUtils.isEmpty(info.getChannel())) {
            channel = info.getChannel();
        } else {
            channel = readChannel_v1(apk);
        }
        return TextUtils.isEmpty(channel) ? DEFAULT_CHANNEL : channel;
    }

    private static boolean isMagicMatched(byte[] buffer) {
        if (buffer.length != MAGIC.length) {
            return false;
        }
        for (int i = 0; i < MAGIC.length; ++i) {
            if (buffer[i] != MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    private static short readShort(DataInput input) throws IOException {
        byte[] buf = new byte[SHORT_LENGTH];
        input.readFully(buf);
        ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
        return bb.getShort(0);
    }

    private static String readChannel_v1(File apk) {
        String channel = "";
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(apk, "r");
            long index = raf.length();
            byte[] buffer = new byte[MAGIC.length];
            index -= MAGIC.length;
            // read magic bytes
            raf.seek(index);
            raf.readFully(buffer);
            // if magic bytes matched
            if (isMagicMatched(buffer)) {
                index -= SHORT_LENGTH;
                raf.seek(index);
                // read content length field
                int length = readShort(raf);
                if (length > 0) {
                    index -= length;
                    raf.seek(index);
                    // read content bytes
                    byte[] bytesComment = new byte[length];
                    raf.readFully(bytesComment);
                    channel = new String(bytesComment, "UTF-8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (raf != null)
                    raf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return channel;
    }

}
