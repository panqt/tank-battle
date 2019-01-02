package pers.panqt.tank;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;

/**
 *  @time       2018年11月19日	3:09
 *	@since      V1.0
 *	@author     panqt
 *	@comment    音乐播放
 */
public class Music {
    File file1;
    AudioClip sound1;
    String string;

    public Music(String string, boolean b) {
        this.string = string;
        file1 = new File(string);
        try {
            sound1 = Applet.newAudioClip(file1.toURI().toURL());
        } catch (MalformedURLException ex) {
        }
        if (b)
            sound1.play();
        else
            sound1.loop();
    }

    public void Close() {
        sound1.stop();
    }
}
