package villagegaulois;

import personnages.Chef;
import personnages.Gaulois;

public class Village {
	private String nom;
	private Chef chef;
	private Gaulois[] villageois;
	private int nbVillageois = 0;
	private Marche marche;

	public Village(String nom, int nbVillageoisMaximum, int nbEtals) {
		this.nom = nom;
		villageois = new Gaulois[nbVillageoisMaximum];
		marche = new Marche(nbEtals);
	}

	public String getNom() {
		return nom;
	}

	public void setChef(Chef chef) {
		this.chef = chef;
	}

	public void ajouterHabitant(Gaulois gaulois) {
		if (nbVillageois < villageois.length) {
			villageois[nbVillageois] = gaulois;
			nbVillageois++;
		}
	}

	public Gaulois trouverHabitant(String nomGaulois) {
		if (nomGaulois.equals(chef.getNom())) {
			return chef;
		}
		for (int i = 0; i < nbVillageois; i++) {
			Gaulois gaulois = villageois[i];
			if (gaulois.getNom().equals(nomGaulois)) {
				return gaulois;
			}
		}
		return null;
	}

	public String afficherVillageois() {
		
		if(chef == null) {
			
			throw new VillageSansChefException();
			
		}
		
		StringBuilder chaine = new StringBuilder();
		if (nbVillageois < 1) {
			chaine.append("Il n'y a encore aucun habitant au village du chef "
					+ chef.getNom() + ".\n");
		} else {
			chaine.append("Au village du chef " + chef.getNom()
					+ " vivent les légendaires gaulois :\n");
			for (int i = 0; i < nbVillageois; i++) {
				chaine.append("- " + villageois[i].getNom() + "\n");
			}
		}
		return chaine.toString();
	}
	
	public String installerVendeur(Gaulois vendeur, String produit, int nbProduit) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(vendeur.getNom()).append(" cherche un endroit pour vendre ").append(nbProduit).append(" ").append(produit).append(".").append('\n');
		
		int index = marche.trouverEtalLibre();
		
		if(index >= 0) {
			
			sb.append("Le vendeur ").append(vendeur.getNom()).append(" vends ").append(nbProduit).append(" ").append(produit).append(" à l'étal n°").append(index).append(".");
			marche.utiliserEtal(index, vendeur, produit, nbProduit);
			
		} else {
			
			sb.append("Il n'y a aucun étal de libre.");
			
		}
		
		return sb.toString();
		
	}
	
	public String rechercherVendeursProduit(String produit) {
		
		StringBuilder sb = new StringBuilder();

		Etal[] etalsTrouves = marche.trouverEtals(produit);
		
		if(etalsTrouves.length == 0) {
			
			sb.append("Il n'y a pas de vendeur qui propose des ").append(produit).append(" au marché.");
			
		} else if(etalsTrouves.length == 1) {
			
			sb.append("Seul le vendeur ").append(etalsTrouves[0].getVendeur().getNom()).append(" propose des ").append(produit).append(" au marché.");	
			
		} else {
			
			sb.append("Les vendeurs qui proposent des ").append(produit).append(" sont :");	
			
			for(Etal etal : etalsTrouves) {
				
				sb.append('\n').append("- ").append(etal.getVendeur().getNom());
				
			}
			
		}
		
		return sb.toString();
		
	}
	
	public Etal rechercherEtal(Gaulois vendeur) {
		
		return marche.trouverVendeur(vendeur);
		
	}
	
	public String partirVendeur(Gaulois vendeur) {
		
		return marche.trouverVendeur(vendeur).libererEtal();
		
	}
	
	public String afficherMarche() {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("Le marché du village ").append('"').append(this.nom).append('"').append(" possède plusieurs étals :").append('\n');
		
		sb.append(marche.afficherMarche());
		
		return sb.toString();
		
	}

	private class Marche {
		
		private Etal[] etals;
		
		public Marche(int nbEtals) {
			
			etals = new Etal[nbEtals];
			
			for(int i = 0; i < nbEtals; i++) {
				
				etals[i] = new Etal();
				
			}
			
		}
		
		public void utiliserEtal(int indiceEtal, Gaulois vendeur, String produit, int nbProduit) {
			
			if(indiceEtal >= 0 || indiceEtal < etals.length) {
				
				etals[indiceEtal].occuperEtal(vendeur, produit, nbProduit);
				
			}

		}
		
		public int trouverEtalLibre() {
			
			for(int i = 0; i < etals.length; i++) {
				
				if(!etals[i].isEtalOccupe())
					return i;
				
			}
			
			return -1;
			
		}
		
		public Etal[] trouverEtals(String produit) {
			
			int matchingEtalsNumber = 0;
			
			for(int i = 0; i < etals.length; i++) {
				
				if(etals[i].isEtalOccupe() && etals[i].contientProduit(produit))
					matchingEtalsNumber++;
				
			}
			
			Etal[] matchingEtals = new Etal[matchingEtalsNumber];
			int matchingEtalsIndex = 0;
			
			for(int i = 0; i < etals.length; i++) {
				
				if(etals[i].isEtalOccupe() && etals[i].contientProduit(produit)) {
					
					matchingEtals[matchingEtalsIndex] = etals[i];
					matchingEtalsIndex++;
					
				}
				
			}
			
			return matchingEtals;
			
		}
		
		public Etal trouverVendeur(Gaulois gaulois) {
			
			for(int i = 0; i < etals.length; i++) {
				
				if(etals[i].getVendeur().equals(gaulois)) {
					
					return etals[i];
					
				}
				
			}
			
			return null;
			
		}
		
		public String afficherMarche() {
			
			StringBuilder sb = new StringBuilder();
			
			int libre = 0;
			
			for(int i = 0; i < etals.length; i++) {
				
				if(etals[i].isEtalOccupe()) {
					
					sb.append(etals[i].afficherEtal());
					
				} else {
					
					libre++;
					
				}
				
			}
			
			if(libre > 0) {
				
				sb.append("Il reste ").append(libre).append(" étals non utilisés dans le marché.");
				
			}
			
			return sb.toString();
			
		}
		
	}
	
}