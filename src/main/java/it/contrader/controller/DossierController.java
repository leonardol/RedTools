package it.contrader.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.contrader.dto.AziendaClienteDTO;
import it.contrader.dto.DossierDTO;
import it.contrader.dto.FatturaDTO;
import it.contrader.dto.TotaleOreReDDTO;
import it.contrader.model.Dossier;
import it.contrader.model.Progetto;
import it.contrader.services.AziendaClienteService;
import it.contrader.services.DossierService;
import it.contrader.services.FatturaService;
import it.contrader.services.ImpiegatoService;
import it.contrader.services.ProgettoService;
import it.contrader.services.TotaleOreReDService;
import it.contrader.utils.GestoreEccezioni;



@Controller
@RequestMapping("/DossierController")
public class DossierController {
	
	//private static final AziendaCliente AziendaCliente = null;
	private final DossierService dossierService;
	private final AziendaClienteService aziendaClienteService;
	private final ProgettoService progettoService;
	private final FatturaService fatturaService;
	private final ImpiegatoService impiegatoService;
	private final TotaleOreReDService totaleOreReDService;
	private HttpSession session;
	
	@Autowired
	public DossierController(DossierService dossierService, AziendaClienteService aziendaClienteService,
			ProgettoService progettoService, FatturaService fatturaService, ImpiegatoService impiegatoService, TotaleOreReDService totaleOreReDService) {
		this.dossierService = dossierService;
		this.aziendaClienteService = aziendaClienteService;
		this.progettoService = progettoService;
		this.fatturaService = fatturaService;
		this.impiegatoService = impiegatoService;
		this.totaleOreReDService = totaleOreReDService;
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
		

		DossierDTO dossierObj = new DossierDTO(0, periodoDiImposta, costoDipendentiPeriodoDiImposta, fatturatoPeriodoDiImposta, numeroTotaleDipendenti, 0, 0, 0, 0, aziendaClienteDTO, progetto, 0);
		
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
		
		DossierDTO dossierObj = new DossierDTO(idDossierUpdate, periodoDiImpostaUpdate, costoDipendentiPeriodoDiImpostaUpdate, fatturatoPeriodoDiImpostaUpdate, numeroTotaleDipendentiUpdate, 0, 0, 0, 0, aziendaCliente, progetto, 0);
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
	
	@RequestMapping(value = "/files/{file_name}", method = RequestMethod.GET)
	public void getFile(
	    @PathVariable("file_name") String fileName, 
	    HttpServletResponse response, HttpServletRequest request) {
		String path="C:\\Users\\Contrader\\Desktop\\Contrader\\RedToolSpring\\src\\main\\resources\\files\\";
		int idProgetto = (int) session.getAttribute("idProgetto");
		Progetto progetto = progettoService.getProgettoById(idProgetto);
		
		int idAziendaCliente = (int) session.getAttribute("idAziendaCliente");
		AziendaClienteDTO aziendaCliente = aziendaClienteService.getAziendaClienteDTOById(idAziendaCliente);
		int id = Integer.parseInt(request.getParameter("id"));
		Dossier dossier = this.dossierService.getDossierById(id);
		List<FatturaDTO> allFattura = this.fatturaService.findFatturaDTOByDossier(dossier);
		allFattura = calcoloTotaleAmmissibileDTO(allFattura, id);
		List<TotaleOreReDDTO> allTotaleOreReDDTO = this.totaleOreReDService.findTotaleOreReDDTOByDossier(dossier);
		allTotaleOreReDDTO = sommaOreReDDTO(allTotaleOreReDDTO, id);
		
		
		try {

		    Workbook workbook;
		    workbook = new XSSFWorkbook(
		        OPCPackage.open(path+"docTables.xlsm")
		    );

		    //DO STUF WITH WORKBOOK
		    Sheet sheet = workbook.getSheetAt(0);
		    Cell cell2Update = sheet.getRow(2).getCell(2);
		    cell2Update.setCellValue(dossier.getPeriodoDiImposta());
		    cell2Update = sheet.getRow(4).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getDenominazioneSocieta());
		    cell2Update = sheet.getRow(5).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getFormaGiuridica());
		    cell2Update = sheet.getRow(7).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getSedeLegale());
		    cell2Update = sheet.getRow(8).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getPartitaIva());
		    cell2Update = sheet.getRow(9).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getTelefono());
		    cell2Update = sheet.getRow(10).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getEmail());
		    cell2Update = sheet.getRow(12).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getIndirizzoUnitaLocale());
		    cell2Update = sheet.getRow(14).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getAttivitaAzienda());
		    cell2Update = sheet.getRow(16).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getLegaleRappresentante());
		    cell2Update = sheet.getRow(17).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getNatoA());
		    cell2Update = sheet.getRow(18).getCell(2);
		    cell2Update.setCellValue(aziendaCliente.getNatoIl());
		    cell2Update = sheet.getRow(20).getCell(2);
		    cell2Update.setCellValue(dossier.getCostoDipendentiPeriodoDiImposta());
		    cell2Update = sheet.getRow(21).getCell(2);
		    cell2Update.setCellValue(dossier.getFatturatoPeriodoDiImposta());
		    cell2Update = sheet.getRow(22).getCell(2);
		    cell2Update.setCellValue(dossier.getNumeroTotaleDipendenti());
		    cell2Update = sheet.getRow(24).getCell(2);
		    double sum = dossier.getTotaleCostiReD() + dossier.getCostiPersonaleReD();
		    cell2Update.setCellValue(sum);
		    cell2Update = sheet.getRow(25).getCell(2);
		    cell2Update.setCellValue(dossier.getCostiPersonaleReD());
		    cell2Update = sheet.getRow(27).getCell(2);
		    cell2Update.setCellValue(progetto.getTitoloProgetto());
		    cell2Update = sheet.getRow(28).getCell(2);
		    cell2Update.setCellValue(progetto.getDettagliProgetto());
		    cell2Update = sheet.getRow(29).getCell(2);
		    cell2Update.setCellValue(progetto.getCoordinateDIIN());
		    cell2Update = sheet.getRow(31).getCell(2);
		    cell2Update.setCellValue(dossier.getTotaleAmmissibile());
		    

		    FileOutputStream out = new FileOutputStream(new File(path+fileName));
		    workbook.write(out);
		    out.close();
		    System.out.println("xlsm created successfully..");

		} catch (FileNotFoundException e) {
		    e.printStackTrace();
		} catch (InvalidFormatException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
		
		// file download 
	    try {
	      // get your file as InputStream
	      InputStream is = new FileInputStream(path+fileName);
	      // copy it to response's OutputStream
	      
	      
	      
	      org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
	      response.flushBuffer();
	    } catch (IOException ex) {
	    	GestoreEccezioni.getInstance().gestisciEccezione(ex);
	    }

	}
	
	@RequestMapping(value = "/visualizzaCostiEsterni", method = RequestMethod.GET)
	public String visualizzaCostiEsterni(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		Dossier dossier = this.dossierService.getDossierById(id);
		List<FatturaDTO> allFattura = this.fatturaService.findFatturaDTOByDossier(dossier);
		request.setAttribute("allFatturaDTO", calcoloTotaleAmmissibileDTO(allFattura, id));
		return "/dossier/visualizzaCostiEsterni";		
	}
	
	@RequestMapping(value = "/visualizzaDipendenti", method = RequestMethod.GET)
	public String visualizzaDipendenti(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		Dossier dossier = this.dossierService.getDossierById(id);
		List<TotaleOreReDDTO> allTotaleOreReD = this.totaleOreReDService.findTotaleOreReDDTOByDossier(dossier);
		request.setAttribute("allTotaleOreReD", sommaOreReDDTO(allTotaleOreReD, id));
		return "/dossier/visualizzaDipendenti";		
	}
	
	@RequestMapping(value = "/readPratica", method = RequestMethod.GET)
	public String leggiPratica(HttpServletRequest request) {
		
		int idAziendaCliente = (int) session.getAttribute("idAziendaCliente");
		AziendaClienteDTO aziendaCliente = aziendaClienteService.getAziendaClienteDTOById(idAziendaCliente);
		request.setAttribute("ReadAziendaCliente", aziendaCliente);
		int id = Integer.parseInt(request.getParameter("id"));
		DossierDTO dossier = this.dossierService.getDossierDTOById(id);
		request.setAttribute("ReadDossierDTO", dossier);
		return "/dossier/readPratica";
	}
	
	
	private void visualDossier(HttpServletRequest request){
		final int idProgetto = (int) session.getAttribute("idProgetto");
		Progetto progetto = progettoService.getProgettoById(idProgetto);
		List<DossierDTO> allDossier = this.dossierService.findDossierDTOByProgetto(progetto);
		request.setAttribute("allDossierDTO", completamentoDTO(allDossier));
	}

	
	
	  
	private List<DossierDTO> completamentoDTO(List<DossierDTO> allDossier) {
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
	
	private List<FatturaDTO> calcoloTotaleAmmissibileDTO(List<FatturaDTO> allFattura, int id) {
		
		List<FatturaDTO> retAllFattura = new ArrayList<FatturaDTO>();
		
		double totaleAmmissibile = 0;
		double totaleCostiReD = 0;
		Set<Integer> fornitori = new HashSet<>(Arrays.asList());
		for(FatturaDTO fattura : allFattura) {
			
			FatturaDTO f = new FatturaDTO();
			f = fattura;
			fornitori.add(f.getFornitore().getIdFornitore());
			f.setTotaleAmmissibile((fattura.getTotaleImponibile() * fattura.getPercentualeAmmissibile()) / 100);
			totaleAmmissibile += f.getTotaleAmmissibile();
			totaleCostiReD += f.getTotaleImponibile();
			retAllFattura.add(f);
			
		}
		
		DossierDTO dossier = this.dossierService.getDossierDTOById(id);
		dossier.setTotaleAmmissibile(totaleAmmissibile);
		dossier.setTotaleCostiReD(totaleCostiReD);
		dossier.setNumeroFornitori(fornitori.size());
		dossierService.updateDossier(dossier);
		
		return retAllFattura;
		
	}
	
	private List<TotaleOreReDDTO> sommaOreReDDTO(List<TotaleOreReDDTO> TotaleOreReDDTO, int id) {
		
		List<TotaleOreReDDTO> retAllTotaleOreReD = new ArrayList<TotaleOreReDDTO>();
		
		double costiPersonaleReD = 0;
		for (TotaleOreReDDTO totaleOreReD : TotaleOreReDDTO ) {
			
			TotaleOreReDDTO totOre = new TotaleOreReDDTO();
			totOre = totaleOreReD;
			double totaleCostiReD = totaleOreReD.getOreLavorateRed() * totaleOreReD.getImpiegato().getCostoOrario();
			costiPersonaleReD += totaleCostiReD; 
			totOre.setTotaleCostiReD(totaleCostiReD);
			retAllTotaleOreReD.add(totOre);
			
		}
		DossierDTO dossier = this.dossierService.getDossierDTOById(id);
		dossier.setCostiPersonaleReD(costiPersonaleReD);
		dossierService.updateDossier(dossier);
		
		return retAllTotaleOreReD;
		
	}
	
	
	
	
	
	
	
	
	
	
}





