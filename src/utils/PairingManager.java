package utils;
/*
 * author: wenzilong,licong
 */
import java.io.InputStream;
import java.net.URL;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.PropertiesParameters;

public class PairingManager {
	private static final String TYPE_A = "../curves/a.properties";

	public static Pairing getDefaultPairing(){
		
		InputStream is = PairingManager.class.getResourceAsStream(TYPE_A);
		return PairingFactory.getPairing(new PropertiesParameters().load(is));
	}
}
