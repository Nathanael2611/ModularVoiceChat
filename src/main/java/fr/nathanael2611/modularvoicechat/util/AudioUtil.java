package fr.nathanael2611.modularvoicechat.util;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Line;
import javax.sound.sampled.Mixer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;

public class AudioUtil
{

    public static final AudioFormat FORMAT = new AudioFormat(48000, 16, 2, true, false);

    public static Mixer findMixer(String name, Line.Info lineInfo)
    {
        Mixer defaultMixer = null;
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
        {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(lineInfo))
            {
                if (mixerInfo.getName().equals(name))
                {
                    return mixer;
                }
                if (defaultMixer == null)
                {
                    defaultMixer = mixer;
                }
            }
        }
        return defaultMixer;
    }

    public static List<String> findAudioDevices(Line.Info lineInfo)
    {
        final List<String> list = new ArrayList<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
        {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(lineInfo))
            {
                list.add(mixerInfo.getName());
            }
        }
        return list;
    }

    public static boolean hasLinesOpen(Mixer mixer)
    {
        return mixer.getSourceLines().length != 0 || mixer.getTargetLines().length != 0;
    }

    public static int calculateVolumeMultiplier(int volume)
    {
        return (int) ((float) Math.tan(volume * 0.0079F) * 10000);
    }

    public static void changeVolume(byte[] pcm, int volume, int multiplier)
    {
        if (volume == 100)
        {
            return;
        }
        changeVolume(pcm, multiplier);
    }

    public static void changeVolume(byte[] pcm, int multiplier)
    {
        final ShortBuffer buffer = ByteBuffer.wrap(pcm).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer();
        for (int index = buffer.position(); index < buffer.limit(); index++)
        {
            buffer.put(index, (short) Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, buffer.get(index) * multiplier / 10000)));
        }
    }

    public static byte[] adjustVolume(byte[] audioSamples, float volume)
    {
        byte[] array = new byte[audioSamples.length];
        for (int i = 0; i < array.length; i += 2)
        {
            // convert byte pair to int
            short buf1 = audioSamples[i + 1];
            short buf2 = audioSamples[i];
            buf1 = (short) ((buf1 & 0xff) << 8);
            buf2 = (short) (buf2 & 0xff);
            short res = (short) (buf1 | buf2);
            res = (short) (res * volume);
            // convert back
            array[i] = (byte) res;
            array[i + 1] = (byte) (res >> 8);

        }
        return array;
    }


    public static double calculateRms(byte[] samples)
    {
        double sumOfSampleSq = 0.0;    // sum of square of normalized samples.
        double peakSample = 0.0;     // peak sample.

        for (short sample : samples)
        {
            double normSample = (double) sample / 32767;  // normalized the sample with maximum value.
            sumOfSampleSq += (normSample * normSample);
            if (Math.abs(sample) > peakSample)
            {
                peakSample = Math.abs(sample);
            }
        }

        double rms = 10 * Math.log10(sumOfSampleSq / samples.length);
        double peak = 20 * Math.log10(peakSample / 32767);
        return rms;
    }

    public static short[] bytesToShorts(byte[] bytes)
    {
        short[] shorts = new short[bytes.length / 2];
        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
        return shorts;
    }

    public static byte[] shortsToBytes(short[] shorts)
    {
        byte[] bytes2 = new byte[shorts.length * 2];
        ByteBuffer.wrap(bytes2).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);
        return bytes2;
    }

    public static float getdB(byte[] buffer)
    {
        double dB = 0.0D;
        short[] shortArray = new short[buffer.length / 2];
        ByteBuffer.wrap(buffer).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shortArray);

        for (short value : shortArray) {
            dB = 20.0D * Math.log10(Math.abs((double) value / 32767.0D));
            if (dB == -1.0D / 0.0 || dB == 0.0D / 0.0) {
                dB = -90.0D;
            }
        }

        float level = (float) dB;
        return level;
    }

    /*
    public static byte[] adjustPitch(byte[] input, float pitch)
    {
        double f = pitch;
        byte[] output = new byte[ceil(input / f)];
        output[0] = input[0]
        for(int i = 1; i < output.length) {
            output[i] = (input[floor(i*factor)] + input[ceil(i*factor)]) / 2;
        }
    }*/

}