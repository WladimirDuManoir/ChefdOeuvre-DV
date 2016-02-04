package daredevil;

import fr.dgac.ivy.Ivy;
import fr.dgac.ivy.IvyClient;
import fr.dgac.ivy.IvyException;
import fr.dgac.ivy.IvyMessageListener;

public class Audio implements IvyMessageListener{

    private Ivy bus;
    private Frame frame;

    public Audio() throws IvyException {
        frame = new Frame();
        bus = new Ivy("IvyTranslater","IvyTranslater Ready",null);
        bus.bindMsg(".*valider.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    switch ((int) (Math.random() * ((2) + 1)) % 3) {
                        case 0:
                            try {
                                bus.sendMsg("ppilot5 Say=\"None valid. Replace the piece.\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            try {
                                bus.sendMsg("ppilot5 Say=\"Can not detect the piece.\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                bus.sendMsg("ppilot5 Say=\"Valid. Good work !\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                else {
                    rienCompris(bus);
                }
            }
        });

        bus.bindMsg(".*bonjour.*Confidence=([0-9],[0-9]*).*", new IvyMessageListener() {
            // callback for "Bye" message
            public void receive(IvyClient client, String[] args) {
                System.out.println(args[0]);
                String s = args[0];
                s = s.replace(',', '.');
                float f = Float.parseFloat(s);
                if (f >= 0.8) {
                    switch ((int) (Math.random() * ((2) + 1)) % 3) {
                        case 0:
                            try {
                                bus.sendMsg("ppilot5 Say=\"Hello and welcome to our first prototype !\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 1:
                            try {
                                bus.sendMsg("ppilot5 Say=\"We already said hello\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 2:
                            try {
                                bus.sendMsg("ppilot5 Say=\"Haahahahahahha\"");
                            } catch (IvyException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                }
                else {
                    rienCompris(bus);
                }
            }
        });
        bus.start(null);
        bus.sendMsg("ppilot5 Say=\"this works\"");

    }

    private void rienCompris(Ivy bus) {
        try {
            bus.sendMsg("ppilot5 Say=\"Debug : I don't understand\"");
        } catch (IvyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receive(IvyClient ivyClient, String[] strings) {

    }

}
