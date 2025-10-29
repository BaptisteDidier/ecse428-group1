package com.ecse428.flowfinder.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.*;

@Entity
public class Registration {

	@EmbeddedId
	private Key key;

	protected Registration() {
	}

	public Registration(Key key) {
		this.key = key;
	}

	public Key getKey() {
		return key;
	}

	@Embeddable
	public static class Key implements Serializable {

		@ManyToOne
		private Client participant;
		@ManyToOne
		private SpecificClass danceClass;

		public Key() {
		}

		public Key(Client participant, SpecificClass danceClass) {
			this.participant = participant;
			this.danceClass = danceClass;
		}

		public SpecificClass getDanceClass() {
			return danceClass;
		}

		public Client getParticipant() {
			return participant;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Key)) {
				return false;
			}
			Key that = (Key) obj;
			return this.participant.getId() == that.participant.getId()
					&& this.danceClass.getId() == that.danceClass.getId();
		}

		@Override
		public int hashCode() {
			return Objects.hash(this.participant.getId(), this.danceClass.getId());
		}

	}
}