package it.contrader.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.contrader.dto.AziendaClienteDTO;
import it.contrader.dto.DossierDTO;
import it.contrader.model.AziendaCliente;
import it.contrader.model.Progetto;
import it.contrader.services.AziendaClienteService;
import it.contrader.services.DossierService;
import it.contrader.services.ProgettoService;



@Controller
@RequestMapping("/DossierController")
public class DossierController {
	
	//private static final AziendaCliente AziendaCliente = null;
	private final DossierService dossierService;
	private final AziendaClienteService aziendaClienteService;
	private final ProgettoService progettoService;
	private HttpSession session;
	
	@Autowired
	public DossierController(DossierService dossierService, AziendaClienteService aziendaClienteService, ProgettoService progettoService) {
		this.dossierService = dossierService;
		this.aziendaClienteService = aziendaClienteService;
		this.progettoService = progettoService;
	}
	
	@RequestMapping(value = "/dossierManagement", method = RequestMethod.GET)
	public String dossierManagement(HttpServletRequest request) {
		session = request.getSession();
		int idProgetto = Integer.parseInt(request.getParameter("id"));
		session.setAttribute("idProgetto", idProgetto);
		visualDossier(request);
		return "/dossier/manageDossier";		
	}
	
	@RequestMapping(value = "/insertRedirect", method = RequestMethod.GET)
	public String insert(HttpServletRequest request) {
		visualDossier(request);
		request.setAttribute("option", "insert");
		return "/dossier/insertDossier";
	}
	
	@RequestMapping(value = "/insert", method = RequestMethod.POST)
	public String insertDossier(HttpServletRequest request) {
		
		String periodoDiImposta = request.getParameter("periodoDiImposta");
		double costoDipendentiPeriodoDiImposta = Double.parseDouble(request.getParameter("costoDipendentiPeriodoDiImposta"));
		double fatturatoPeriodoDiImposta = Double.parseDouble(request.getParameter("fatturatoPeriodoDiImposta"));
		int numeroTotaleDipendenti = Integer.parseInt(request.getParameter("numeroTotaleDipendenti"));
		
		int idProgetto = (int) session.getAttribute("idProgetto");
		Progetto progetto = progettoService.getProgettoById(idProgetto);
		
		int idAziendaCliente = (int) session.getAttribute("idAziendaCliente");
		AziendaClienteDTO aziendaClienteDTO = aziendaClienteService.getAziendaClienteDTOById(idAziendaCliente);
		

		DossierDTO dossierObj = new DossierDTO(0, periodoDiImposta, costoDipendentiPeriodoDiImposta, fatturatoPeriodoDiImposta, numeroTotaleDipendenti, aziendaClienteDTO, progetto, 0);
		
		dossierService.insertDossier(dossierObj);

		visualDossier(request);
		return "/dossier/manageDossier";
	}
	
	
	
	@RequestMapping(value = "/updateRedirect", method = RequestMethod.GET)
	public String updateRedirect(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		DossierDTO dossierUpdate = new DossierDTO();
		
		dossierUpdate = this.dossierService.getDossierDTOById(id);
		request.setAttribute("dossierUpdate", dossierUpdate);
		return "/dossier/updateDossier";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String update(HttpServletRequest request) {
		
		Integer idDossierUpdate = Integer.parseInt(request.getParameter("dossierId"));
		String periodoDiImpostaUpdate = request.getParameter("periodoDiImposta");
		double costoDipendentiPeriodoDiImpostaUpdate = Double.parseDouble(request.getParameter("costoDipendentiPeriodoDiImposta"));
		double fatturatoPeriodoDiImpostaUpdate = Double.parseDouble(request.getParameter("fatturatoPeriodoDiImposta"));
		int numeroTotaleDipendentiUpdate = Integer.parseInt(request.getParameter("numeroTotaleDipendenti"));
		
		int idProgetto = (int) session.getAttribute("idProgetto");
		Progetto progetto = progettoService.getProgettoById(idProgetto);
		
		int idAziendaCliente = (int) session.getAttribute("idAziendaCliente");
		AziendaClienteDTO aziendaCliente = aziendaClienteService.getAziendaClienteDTOById(idAziendaCliente);
		
		DossierDTO dossierObj = new DossierDTO(idDossierUpdate, periodoDiImpostaUpdate, costoDipendentiPeriodoDiImpostaUpdate, fatturatoPeriodoDiImpostaUpdate, numeroTotaleDipendentiUpdate, aziendaCliente, progetto, 0);
		dossierService.updateDossier(dossierObj);
		visualDossier(request);
		return "/dossier/manageDossier";
	}
	
	
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		request.setAttribute("id", id);
		this.dossierService.deleteDossierById(id);
		visualDossier(request);
		return "/dossier/manageDossier" ;
		
	}	
	
	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public String leggiDossier(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		DossierDTO dossier = this.dossierService.getDossierDTOById(id);
		request.setAttribute("ReadDossierDTO", dossier);
//		visualDossier(request);
		
		return "/dossier/readDossier";
	}
	
	
	
	private void visualDossier(HttpServletRequest request){
		final int idProgetto = (int) session.getAttribute("idProgetto");
		Progetto progetto = progettoService.getProgettoById(idProgetto);
		List<DossierDTO> allDossier = this.dossierService.findDossierDTOByProgetto(progetto);
		request.setAttribute("allDossierDTO", CompletamentoDTO(allDossier));
	}

	
	
	  
	private List<DossierDTO> CompletamentoDTO(List<DossierDTO> allDossier) {
		List<DossierDTO> retAllDossier = new ArrayList<DossierDTO>();
		
		
		for(DossierDTO dossier : allDossier) {
			int y=2;
			if((dossier.getPeriodoDiImposta() != null && dossier.getPeriodoDiImposta().equals("")));
				y++;
			if((dossier.getCostoDipendentiPeriodoDiImposta() !=0))
				y++;
			if(dossier.getFatturatoPeriodoDiImposta() !=0)
				y++;
			if(dossier.getNumeroTotaleDipendenti() !=0)
				y++;
			DossierDTO d = new DossierDTO();
			d = dossier;
			d.setFilledFields(y);
			retAllDossier.add(d);
			
		}
		return retAllDossier;
	
	                     
	
	
}
}
