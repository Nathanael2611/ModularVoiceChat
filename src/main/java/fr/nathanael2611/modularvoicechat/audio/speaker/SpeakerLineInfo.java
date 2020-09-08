package fr.nathanael2611.modularvoicechat.audio.speaker;

import fr.nathanael2611.modularvoicechat.util.AudioUtil;

import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

/**
 *
 * Based on: https://github.com/MC-U-Team/Voice-Chat/blob/1.15.2/audio-client/src/main/java/info/u_team/voice_chat/audio_client/speaker/SpeakerLineInfo.java
 */
public class SpeakerLineInfo
{

    private final SourceDataLine line;

    private long lastAccessed;

    private boolean masterVolumeControlFound;
    private int multiplier;

    public SpeakerLineInfo(SourceDataLine line)
    {
        this.line = line;
        lastAccessed = System.currentTimeMillis();
    }

    public void setMasterVolume(int volume)
    {
        multiplier = AudioUtil.calculateVolumeMultiplier(volume);
    }

    public SourceDataLine getSourceDataLine()
    {
        lastAccessed = System.currentTimeMillis();
        return line;
    }

    public long getLastAccessed()
    {
        return lastAccessed;
    }

    public boolean isMasterVolumeControlFound()
    {
        return masterVolumeControlFound;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

}
