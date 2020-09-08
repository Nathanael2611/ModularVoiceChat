package fr.nathanael2611.modularvoicechat.audio.micro;

import javax.sound.sampled.*;

import fr.nathanael2611.modularvoicechat.audio.api.NoExceptionCloseable;
import fr.nathanael2611.modularvoicechat.util.AudioUtil;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/micro/MicroData.java#L8
 */
public class MicroData implements NoExceptionCloseable
{

    public static final DataLine.Info MIC_INFO = new DataLine.Info(TargetDataLine.class, AudioUtil.FORMAT);

    private Mixer mixer;
    private TargetDataLine targetLine;

    private int volume;
    private int multiplier;

    public MicroData(String microName, int volume)
    {
        setMixer(microName);
        setVolume(volume);
    }

    private boolean createLine()
    {
        if (mixer != null)
        {
            try
            {
                final TargetDataLine line = (TargetDataLine) mixer.getLine(MIC_INFO);
                line.open(AudioUtil.FORMAT, 960 * 2 * 2 * 4);
                line.start();
                targetLine = line;
                return true;
            } catch (LineUnavailableException ignored)
            {
            }
        }
        return false;
    }

    private void closeLine()
    {
        if (targetLine != null)
        {
            targetLine.flush();
            targetLine.stop();
            targetLine.close();
        }
    }

    public String getMixer()
    {
        if (mixer != null)
        {
            return mixer.getMixerInfo().getName();
        }
        return null;
    }

    public void setMixer(String name)
    {
        if (mixer != null && mixer.getMixerInfo().getName().equals(name))
        {
            return;
        }
        final Mixer oldMixer = mixer;
        mixer = AudioUtil.findMixer(name, MIC_INFO);
        closeLine();
        targetLine = null;
        if (oldMixer != null)
        {
            if (!AudioUtil.hasLinesOpen(oldMixer))
            {
                oldMixer.close();
            }
        }
    }

    public int getVolume()
    {
        return volume;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
        multiplier = AudioUtil.calculateVolumeMultiplier(volume);
    }

    public boolean isAvailable()
    {
        if (mixer != null)
        {
            if (targetLine != null)
            {
                return targetLine.isOpen();
            } else
            {
                return createLine();
            }
        }
        return false;
    }

    public void flush()
    {
        if (isAvailable())
        {
            targetLine.flush();
        }
    }

    public byte[] read(byte[] array)
    {
        if (isAvailable())
        {
            targetLine.read(array, 0, array.length);
            AudioUtil.changeVolume(array, volume, multiplier);
        }
        return array;
    }

    @Override
    public void close()
    {
        closeLine();
        if (mixer != null)
        {
            if (!AudioUtil.hasLinesOpen(mixer))
            {
                mixer.close();
            }
        }
    }

}
