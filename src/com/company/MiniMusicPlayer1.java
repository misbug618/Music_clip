package com.company;

import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;

public class MiniMusicPlayer1 {

    static JFrame f = new JFrame("Мой первый музыкальный клип");
    static MyDrawPanel ml;

    public static void main(String[] args) {
        MiniMusicPlayer1 mini = new MiniMusicPlayer1();
        mini.go();
    }

    public void setUpGui() {
        ml = new MyDrawPanel();
        f.setContentPane(ml);
        f.setBounds(30, 30, 300, 300);
        f.setVisible(true);
    }

    public void go() {
        setUpGui();

        try {
            Sequencer sequencer = MidiSystem.getSequencer(); // создаем синтезатор
            sequencer.open();                                // открываем синтезатор
            sequencer.addControllerEventListener(ml, new int[]{127});
            Sequence seq = new Sequence(Sequence.PPQ, 4);    // создаем последовательность
            Track track = seq.createTrack();                 // создаем дорожку

            int r = 0;
            for (int i = 0; i < 60; i += 4) {                // создаем рандомные ноты
                r = (int) ((Math.random() * 50) + 1);
                track.add(makeEvent(144, 1, r, 100, i));
                track.add(makeEvent(176, 1, 127, 0, i));
                track.add(makeEvent(128, 1, r, 100, i + 2));
            }


            //запускаем синтезатор
            sequencer.setSequence(seq);
            sequencer.start();
            sequencer.setTempoInBPM(120);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // вспомогательный статический метод, который создает сообщения и возвращает MidiEvent
    public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comd, chan, one, two);
            event = new MidiEvent(a, tick);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return event;
    }

    class MyDrawPanel extends JPanel implements ControllerEventListener {
        boolean msg = false; // присваиваем флагу значение false и будем уставливать true, когда получим событие

        public void controlChange(ShortMessage event) { // метод обработки события
            msg = true;
            repaint();
        }

        public void paintComponent(Graphics g) {
            if (msg) { // мы должны использовать флагб потому что другие объекты мошут запустить repaint(),
                       // а мы хотим рисовать только тогда, когда произойдет событие ControllerEvent

                Graphics2D g2 = (Graphics2D) g;

                // генерация случайного цвета и рисование полупроизвольного прямоугольника
                int r = (int) (Math.random() * 250);
                int gr = (int) (Math.random() * 250);
                int b = (int) (Math.random() * 250);

                g.setColor(new Color(r, gr, b));

                int ht = (int) ((Math.random() * 120) + 10);
                int width = (int) ((Math.random() * 120) + 10);

                int x = (int) ((Math.random() * 40) + 10);
                int y = (int) ((Math.random() * 40) + 10);

                g.fillRect(x, y, ht, width);
                msg = false;
            }
        }
    }
}