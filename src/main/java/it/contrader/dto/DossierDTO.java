package it.contrader.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class DossierDTO {
	
	private int idDossier;
	private String periodoDiImposta;
	private int costoDipendentiPeriodoDiImposta;
	private double fatturatoPeriodoDiImposta;
	private int numeroTotaleDipendenti;
	private double costoComplessivo;
	private double costoPersonale;
	private int idProgetto;
	private int idAziendaCliente;
	private int filledFields; 
	
	

}
