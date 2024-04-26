package com.ferme.itservices.common;

import com.ferme.itservices.models.Client;

import java.util.ArrayList;
import java.util.List;

public class ClientConstants {
	public static final Client FELIPE = new Client(1L, "Ferme", "21986861613", "Tijuca", "Rua Silva Perez 39/21", "Amigo do Jaca", null);
	public static final Client JOAO = new Client(2L, "João", "21986831413", "Méier", "Rua Adolfo 2334", "Irmão do Jorel", null);
	public static final Client RONALDO = new Client(3L, "Ronaldo", "21982831413", "Penha", "Rua Silva Cruz 33/200", "Pai do Sandro", null);

	public static final Client EMPTY_CLIENT = new Client();
	public static final Client INVALID_CLIENT = new Client(null, "234423232", "21986861613333", "", "", "", null);

	public static final List<Client> CLIENTS = new ArrayList<>() {
		{
			add(FELIPE);
			add(JOAO);
			add(RONALDO);
		}
	};
}