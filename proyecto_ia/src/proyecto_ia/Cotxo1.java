/*
Acabar de pulir lo de poner aceite, ya que solo lo hace una vez
Ver si se puede hacer que no colisione laterlmente(no se si se puede hacer)
customizar el coche   
Podemos poner que vaya a por los puntos verdes y azules

Un breu document(presentació powerpoint)que descrigui el comportament del vostre
cotxe i les particularitats que hagueu pogut implementar. 
El document ha de resumir el comportament que s'espera de l'agent.
oLaclasse CotxoN(.java) i les classes (.class) corresponents
que hagueu implementat(N és el número de grup que teniu assignat).
Lavostra implementació obligatòriament ha d'incloure:
§Laimatge personalitzadaen format gif o pgn per al vostre agent
(el podeu definiral constructor del cotxo)§Elnom únic identificatiu del vostre
cotxe durant el combat(el podeu definiralconstructor del cotxo
 */
package agents;

/**
 *
 * @author Ramon
 */
// Exemple de Cotxo molt bàsic
public class Cotxo1 extends Agent {

    static final boolean DEBUG = false;

    static final int ESQUERRA = 0;
    static final int CENTRAL = 1;
    static final int DRETA = 2;
    static final int COTXE = 1;
    static final int PARET = 0;

    int VELOCITATTOPE = 5;
    int VELOCITATFRE = 3;

    Estat estat;
    int espera = 0;

    double desquerra, ddreta, dcentral;

    public Cotxo1(Agents pare) {
        super(pare, "Cotxo1", "imatges/CotxoV.png");
    }

    @Override
    public void inicia() {
        setAngleVisors(40);
        setDistanciaVisors(350);
        setVelocitatAngular(9);
    }

    @Override
    public void avaluaComportament() {

        estat = estatCombat();  // Recuperam la informació actualitzada de l'entorn
        if (estat.fuel == 0) {
            atura();
        } else {
            // Si volem repetir una determinada acció durant varies interaccions
            // ho hem de gestionar amb una variable (per exemple "espera") que faci
            // l'acció que volem durant el temps que necessitem

            if (espera > 0) {  // no facis res, continua amb el que estaves fent
                espera--;
                return;
            } else {
                //Colisión frontal
                if (estat.enCollisio && estat.distanciaVisors[CENTRAL] < 15) { // evita fer-ho marxa enrera
                    noGiris();

                    if (estat.distanciaVisors[CENTRAL] > 20) {
                        endavant(4);
                        return;
                    }

                    enrere(4);
                    espera = 30;
                    return;

                } else if (estat.enCollisio) {
                    noGiris();
                    //setAngleVisors(89);
                    enrere(4);
                    System.out.println("Distancia izquierda: " + estat.distanciaVisors[ESQUERRA]);
                    System.out.println("Distancia derecha: " + estat.distanciaVisors[DRETA]);
                    System.out.println("Distancia central: " + estat.distanciaVisors[CENTRAL]);
                    if (estat.distanciaVisors[ESQUERRA] < 80) {
                        dreta();
                        //endavant(2);
                    } else if (estat.distanciaVisors[DRETA] < 80) {
                        esquerra();
                        //endavant(2);
                    }
                    espera = 15;
                    //setAngleVisors(40);
                    return;
                }

                ddreta = estat.distanciaVisors[DRETA];
                desquerra = estat.distanciaVisors[ESQUERRA];
                dcentral = estat.distanciaVisors[CENTRAL];
                //

                //
                /**
                 * Movilidad del coche con obstaculos laterales o frontales Si
                 * ve un objeto muy lejano en el v. central, o no ve ninguno
                 * adelante Si por su lado hay algo (rival o mancha de aceite),
                 * si es asi que lo esquive. O si ve algo cerca en el v.
                 * central, se cercina si es un rival o una mancha de aceite, y
                 * lo esquive
                 */
                //Seguir adelante
                if (dcentral > 170) { // No hay obstaculos de frente, nada que corte el visor a menos de 170
                    endavant(VELOCITATTOPE);
                }

                /* //Esquivar obstaculo izquierdo
                if (estat.objecteVisor[ESQUERRA] == COTXE || (estat.objecteVisor[ESQUERRA] == 2 && estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.TACAOLI)) {
                    dreta();
                    //Esquivar obstaculo derecho
                } else if (estat.objecteVisor[DRETA] == COTXE || (estat.objecteVisor[DRETA] == 2 && estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.TACAOLI)) {
                    esquerra();
                } else {
                    for (int i = 1; i < 5; i++) {
                        int obj = estat.sector[i];
                        System.out.println("antes del switch: " + obj);
                        switch (obj) {
                            //estat.objectes[estat.indexObjecte[DRETA]].sector condicion del switch
                            case 1:

                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                        }
                    }

                }*/
                //Esquivar obstaculo central, dependiendo de la distancia lateral con la pared
                if (dcentral <= 70) {//70
                    if (estat.objecteVisor[CENTRAL] == COTXE || (estat.objecteVisor[CENTRAL] == 2 && estat.objectes[estat.indexObjecte[CENTRAL]].tipus == Agent.TACAOLI)) {
                        if (estat.objecteVisor[ESQUERRA] == PARET && desquerra < 25) {
                            dreta();

                        } else if (estat.objecteVisor[DRETA] == PARET && ddreta < 25) {
                            esquerra();

                        }
                    } else if (estat.objecteVisor[CENTRAL] == 2) {
                        if (estat.objectes[estat.indexObjecte[CENTRAL]].tipus == Agent.TACAOLI) {
                            dreta();
                        } else if (estat.objectes[estat.indexObjecte[CENTRAL]].tipus == Agent.OLI
                                || estat.objectes[estat.indexObjecte[CENTRAL]].tipus == Agent.RECURSOS) {
                            endavant(4);
                        }
                    }
                } else {
                    if (estat.objecteVisor[CENTRAL] == 2) {
                        switch (estat.objectes[estat.indexObjecte[CENTRAL]].tipus) {
                            case Agent.TACAOLI:
                                dreta();
                                break;
                            case Agent.OLI:
                                endavant(4);
                                System.out.println("OLI ENCONTRADO");
                                break;
                            case Agent.RECURSOS:
                                endavant(4);
                                System.out.println("RECURSOS ENCONTRADOS");
                                break;
                            default:
                                endavant(4);
                                break;
                        }
                    }
                }

                // ESQUIVAR OBSTACULO IZQUIERDA
                if (estat.objecteVisor[ESQUERRA] == COTXE) {
                    dreta();
                } else if (estat.objecteVisor[ESQUERRA] == 2) {
                    if (estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.TACAOLI) {
                        dreta();
                    } else if (estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.OLI
                            || estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.RECURSOS) {
                        esquerra();
                    }
                } else if (estat.objecteVisor[DRETA] == COTXE) { // ESQUIVAR OBSTACULO DERECHA
                    esquerra();
                } else if (estat.objecteVisor[DRETA] == 2) {
                    if (estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.TACAOLI) {
                        esquerra();
                    } else if (estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.OLI
                            || estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.RECURSOS) {

                        dreta();
                    }
                }

                //CONTRADIRECCIÓN
                /**
                 * Semejante a la colision, le decimos que si detecta que esta
                 * en contradirección se gire hacia la derecha, y le decimos que
                 * espere por si sigue encontr dirección
                 */
                if (espera > 0) {
                    espera--;
                    return;
                } else {

                    if (estat.contraDireccio) {
                        if (ddreta < 20 && estat.objecteVisor[DRETA] == PARET) {
                            esquerra();
                            // return;
                        } else if (desquerra < 20 && estat.objecteVisor[ESQUERRA] == PARET) {
                            dreta();
                            //return;
                        } else {
                            dreta();

                        }
                        espera = 10;
                        return;
                    }
                }
                //Buscar id del rival
                int id_rival, sector_rival;
                if (estat.id != 0) {
                    id_rival = 0;
                } else {
                    id_rival = 1;
                }
                //Seguir el rival para poder disparar
                sector_rival = estat.sector[id_rival];
                if (sector_rival == 2) {
                    dreta();
                    System.out.println("Encontre rival ");

                } else if (sector_rival == 3) {
                    esquerra();
                    System.out.println("Encontre rival ");

                }

                //Disparar
                /**
                 * Dispar al rival en un rango entr 50 y 350, para poder
                 * gestionar las balas y no haga gaste tan rapido
                 */
                if (estat.bales > 0) {
                    if (estat.objecteVisor[CENTRAL] == COTXE) {
                        if (50 < dcentral && dcentral < 350) {
                            dispara();
                        }
                        if (dcentral < 60) {
                            if (ddreta >= 30 && (estat.sector[2] == COTXE || estat.sector[2] == TACAOLI)) {
                                esquerra();
                            } else if (desquerra >= 30 && (estat.sector[2] == COTXE || estat.sector[2] == TACAOLI)) {
                                dreta();
                            }
                        }
                    }
                }

                // Per si vull anar el més recte possible: no sempre és la manera més ràpida
                if ((desquerra > 40) && (ddreta > 40) && dcentral > 180) {
                    endavant(VELOCITATTOPE);
                    noGiris();
                    return;
                }

                if (ddreta > desquerra) {
                    dreta();
                    if (estat.oli > 0) {
                        posaOli();
                    }

                } else {
                    esquerra();
                    if (estat.oli > 0) {
                        posaOli();
                    }
                }
                endavant(VELOCITATFRE);
            }

        }
    }
}
