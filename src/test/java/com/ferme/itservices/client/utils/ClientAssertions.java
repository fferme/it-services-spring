package com.ferme.itservices.client.utils;

import com.ferme.itservices.api.dtos.ClientDTO;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ClientAssertions {
	public static ClientAssertions instance;

	private ClientAssertions() { }

	public static ClientAssertions getInstance() {
		return (instance == null)
			? instance = new ClientAssertions()
			: instance;
	}

	public void assertClientProps(ClientDTO expectedClientDTO, ClientDTO sut) {
		assertThat(sut).isNotNull();
		assertThat(sut.name()).isEqualTo(expectedClientDTO.name());
		assertThat(sut.phoneNumber()).isEqualTo(expectedClientDTO.phoneNumber());
		assertThat(sut.neighborhood()).isEqualTo(expectedClientDTO.neighborhood());
		assertThat(sut.address()).isEqualTo(expectedClientDTO.address());
		assertThat(sut.reference()).isEqualTo(expectedClientDTO.reference());
	}

	public void assertClientListProps(List<ClientDTO> expectedList, List<ClientDTO> sut) {
		assertThat(sut).isNotEmpty();
		assertThat(sut).hasSameSizeAs(expectedList);

		for (int i = 0; i < sut.size(); i++) {
			assertClientProps(expectedList.get(i), sut.get(i));
		}
		assertThat(sut).isSortedAccordingTo(Comparator.comparing(ClientDTO::name));
	}

}
