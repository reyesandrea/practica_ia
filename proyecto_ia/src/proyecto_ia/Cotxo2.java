/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

/**
 *
 * @author Ramon
 */

// Exemple de Cotxo molt bàsic


public class Cotxo2 extends Agent {

    static final boolean DEBUG = false;

    static final int ESQUERRA = 0;
    static final int CENTRAL = 1;
    static final int DRETA = 2;
    static final int COTXE = 1;
    static final int PARET=0;
    int VELOCITATTOPE = 5;
    int VELOCITATFRE = 3;

    Estat estat;
    int espera = 0;

    double desquerra, ddreta, dcentral;


    public Cotxo2(Agents pare) {
        super(pare, "Rival", "imatges/cotxe2.gif");
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

        // Si volem repetir una determinada acció durant varies interaccions
        // ho hem de gestionar amb una variable (per exemple "espera") que faci
        // l'acció que volem durant el temps que necessitem
        
        if (espera > 0) {  // no facis res, continua amb el que estaves fent
            espera--;
            return;
        } else {
                  
            if (estat.enCollisio && estat.distanciaVisors[CENTRAL] < 15) // evita fer-ho marxa enrera
            {
                noGiris();

                if (estat.distanciaVisors[CENTRAL] > 20) {
                    endavant(4);
                    return;
                }

                enrere(4);
                espera = 30;
                return;
            }

            ddreta = estat.distanciaVisors[DRETA];
            desquerra = estat.distanciaVisors[ESQUERRA];
            dcentral = estat.distanciaVisors[CENTRAL];
            if (dcentral > 170) {
                endavant(VELOCITATTOPE);
            }
            //Esquivar obstaculo central, dependiendo de la distancia lateral con la pared
                if (dcentral <= 70) {//70
                    if (estat.objecteVisor[CENTRAL] == COTXE || (estat.objecteVisor[CENTRAL] == 2 && estat.objectes[estat.indexObjecte[CENTRAL]].tipus == Agent.TACAOLI)) {
                        if (estat.objecteVisor[ESQUERRA] == PARET && desquerra < 30) {
                            dreta();
                           
                        } else if (estat.objecteVisor[DRETA] == PARET && ddreta < 30) {
                            esquerra();
                            
                        }
                    }
                }
                
                   
                if (estat.objecteVisor[ESQUERRA] == COTXE) {
                    dreta();
                } else if (estat.objecteVisor[ESQUERRA] == 2) {
                    if (estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.TACAOLI) {
                        dreta();
                    } else if (estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.OLI
                            || estat.objectes[estat.indexObjecte[ESQUERRA]].tipus == Agent.RECURSOS) {
                        esquerra();
                    }
                }
                else if (estat.objecteVisor[DRETA] == COTXE) {
                    esquerra();
                } else if (estat.objecteVisor[DRETA] == 2) {
                    if (estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.TACAOLI) {
                        esquerra();
                    }
                    else if (estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.OLI
                            || estat.objectes[estat.indexObjecte[DRETA]].tipus == Agent.RECURSOS) {
                        dreta();
                    }
                }

            
            
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

            // Per si vull anar el més recte possible: no sempre és la manera més ràpida
            if ((desquerra > 40) && (ddreta > 40) && dcentral > 180) {
                endavant(VELOCITATTOPE);
                noGiris();
                return;
            }

            if (ddreta > desquerra) {
                dreta(); posaOli();
            } else {
                esquerra(); posaOli();
            } 
            endavant(VELOCITATFRE);
        }
    }
}

