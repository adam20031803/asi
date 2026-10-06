import os
import sys
from reportlab.lib.pagesizes import A4
from reportlab.lib import colors
from reportlab.lib.units import cm, mm
from reportlab.lib.styles import getSampleStyleSheet, ParagraphStyle
from reportlab.platypus import (
    SimpleDocTemplate, Paragraph, Spacer, Table, TableStyle, PageBreak, KeepTogether, HRFlowable
)
from reportlab.pdfgen import canvas

class NumberedCanvas(canvas.Canvas):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self._saved_page_states = []

    def showPage(self):
        self._saved_page_states.append(dict(self.__dict__))
        self._startPage()

    def save(self):
        num_pages = len(self._saved_page_states)
        for state in self._saved_page_states:
            self.__dict__.update(state)
            self.draw_page_number(num_pages)
            super().showPage()
        super().save()

    def draw_page_number(self, page_count):
        self.saveState()
        self.setFont("Helvetica", 8)
        self.setFillColor(colors.HexColor("#64748B"))
        
        # Header (pages > 1)
        if self._pageNumber > 1:
            self.drawString(2 * cm, 28.5 * cm, "ESPRIT — Architecture des Systèmes d'Information | Atelier 3 : Spring Data JPA")
            self.setStrokeColor(colors.HexColor("#CBD5E1"))
            self.setLineWidth(0.5)
            self.line(2 * cm, 28.3 * cm, 19 * cm, 28.3 * cm)
            
        # Footer
        self.setStrokeColor(colors.HexColor("#CBD5E1"))
        self.setLineWidth(0.5)
        self.line(2 * cm, 1.8 * cm, 19 * cm, 1.8 * cm)
        
        self.drawString(2 * cm, 1.3 * cm, "Projet AutoLoc API — Rapport & Guide d'Exécution Atelier 3")
        page_str = f"Page {self._pageNumber} / {page_count}"
        self.drawRightString(19 * cm, 1.3 * cm, page_str)
        self.restoreState()

def create_pdf(filename="Atelier_3_Spring_Data_JPA_Rapport.pdf"):
    doc = SimpleDocTemplate(
        filename,
        pagesize=A4,
        leftMargin=1.8 * cm,
        rightMargin=1.8 * cm,
        topMargin=2.2 * cm,
        bottomMargin=2.2 * cm
    )

    styles = getSampleStyleSheet()

    # Custom color palette
    c_primary = colors.HexColor("#1E3A8A")     # Navy Blue
    c_secondary = colors.HexColor("#0D9488")   # Teal
    c_dark = colors.HexColor("#0F172A")        # Dark slate
    c_light_bg = colors.HexColor("#F8FAFC")    # Very light gray/blue
    c_accent_bg = colors.HexColor("#EFF6FF")   # Light blue tint
    c_warn_bg = colors.HexColor("#FEF3C7")     # Light amber
    c_warn_border = colors.HexColor("#D97706") # Amber border
    c_code_bg = colors.HexColor("#1E293B")     # Dark slate for code
    c_code_text = colors.HexColor("#F8FAFC")

    # Typography Styles
    title_style = ParagraphStyle(
        'CoverTitle',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=22,
        leading=26,
        textColor=c_primary,
        alignment=1, # Center
        spaceAfter=8
    )

    subtitle_style = ParagraphStyle(
        'CoverSubtitle',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=13,
        leading=17,
        textColor=c_secondary,
        alignment=1,
        spaceAfter=15
    )

    h1_style = ParagraphStyle(
        'CustomH1',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=15,
        leading=19,
        textColor=c_primary,
        spaceBefore=14,
        spaceAfter=8,
        keepWithNext=True
    )

    h2_style = ParagraphStyle(
        'CustomH2',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=12,
        leading=16,
        textColor=c_secondary,
        spaceBefore=10,
        spaceAfter=5,
        keepWithNext=True
    )

    body_style = ParagraphStyle(
        'CustomBody',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=c_dark,
        spaceAfter=6
    )

    bullet_style = ParagraphStyle(
        'CustomBullet',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9.5,
        leading=13.5,
        textColor=c_dark,
        leftIndent=14,
        spaceAfter=4
    )

    callout_style = ParagraphStyle(
        'CalloutText',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=9,
        leading=13,
        textColor=colors.HexColor("#1E293B")
    )

    code_style = ParagraphStyle(
        'CodeSnippet',
        parent=styles['Normal'],
        fontName='Courier',
        fontSize=8.5,
        leading=11.5,
        textColor=c_code_text
    )

    tbl_header_style = ParagraphStyle(
        'TableHeader',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=9,
        leading=12,
        textColor=colors.white,
        alignment=1
    )

    tbl_cell_style = ParagraphStyle(
        'TableCell',
        parent=styles['Normal'],
        fontName='Helvetica',
        fontSize=8.5,
        leading=11.5,
        textColor=c_dark
    )

    tbl_cell_bold = ParagraphStyle(
        'TableCellBold',
        parent=styles['Normal'],
        fontName='Helvetica-Bold',
        fontSize=8.5,
        leading=11.5,
        textColor=c_primary
    )

    story = []

    # ==================== PAGE DE COUVERTURE / EN-TÊTE ====================
    story.append(Spacer(1, 10))
    
    # Header badge table
    badge_data = [
        [Paragraph("<b>ÉCOLE SUPÉRIEURE PRIVÉE D'INGÉNIERIE ET DE TECHNOLOGIES (ESPRIT)</b><br/>Unité Pédagogique : Architecture des Systèmes d'Information (UP ASI)", ParagraphStyle('Badge', parent=body_style, fontSize=8.5, leading=11, alignment=1, textColor=colors.HexColor("#475569")))]
    ]
    badge_table = Table(badge_data, colWidths=[17.4 * cm])
    badge_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), colors.HexColor("#F1F5F9")),
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#CBD5E1")),
        ('PADDING', (0,0), (-1,-1), 8),
        ('ALIGN', (0,0), (-1,-1), 'CENTER'),
    ]))
    story.append(badge_table)
    story.append(Spacer(1, 15))

    story.append(Paragraph("ATELIER 3 : SPRING DATA JPA", title_style))
    story.append(Paragraph("Interfaces Repository, Opérations CRUD et Analyse Qualité SonarQube", subtitle_style))
    
    story.append(HRFlowable(width="100%", thickness=1.5, color=c_primary, spaceBefore=4, spaceAfter=12))

    # Meta Info Card
    meta_data = [
        [Paragraph("<b>Projet :</b> AutoLoc API (Microservice de Gestion de Location de Véhicules)", tbl_cell_style), Paragraph("<b>Niveau :</b> ASI 26-27", tbl_cell_style)],
        [Paragraph("<b>Technologies :</b> Java 17+, Spring Boot 3, Spring Data JPA, Hibernate, MySQL, Lombok", tbl_cell_style), Paragraph("<b>Outils :</b> IntelliJ IDEA, SonarQube for IDE (SonarLint)", tbl_cell_style)],
        [Paragraph("<b>Package analysé :</b> <code>tn.esprit.autoloc.repository</code>", tbl_cell_style), Paragraph("<b>Auteur :</b> Rapport TP Réalisé", tbl_cell_style)]
    ]
    meta_table = Table(meta_data, colWidths=[9.5 * cm, 7.9 * cm])
    meta_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), c_accent_bg),
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#BFDBFE")),
        ('PADDING', (0,0), (-1,-1), 6),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
    ]))
    story.append(meta_table)
    story.append(Spacer(1, 14))

    # ==================== 1. OBJECTIFS & CONTEXTE ====================
    story.append(Paragraph("1. Objectifs Pédagogiques & Contexte de l'Atelier", h1_style))
    story.append(Paragraph(
        "Dans le cadre du développement de l'application <b>AutoLoc</b>, l'Atelier 2 a permis de modéliser les neuf entités "
        "du domaine métier avec leurs relations JPA (OneToOne, OneToMany, ManyToOne, ManyToMany). "
        "L'objectif de cet <b>Atelier 3</b> est de mettre en place la <b>couche Repository</b> afin d'assurer la persistance et la manipulation "
        "de ces entités en base de données MySQL, tout en appliquant les meilleures pratiques de développement et d'analyse statique.",
        body_style
    ))
    
    story.append(Paragraph("<b>Les principaux axes traités dans cet atelier sont :</b>", body_style))
    story.append(Paragraph("• <b>Compréhension de Spring Data JPA :</b> Architecture de la couche de persistance et génération dynamique de proxies.", bullet_style))
    story.append(Paragraph("• <b>Hiérarchie des interfaces :</b> Rôle précis de <code>Repository</code>, <code>CrudRepository</code>, <code>ListCrudRepository</code>, <code>PagingAndSortingRepository</code> et <code>JpaRepository</code>.", bullet_style))
    story.append(Paragraph("• <b>Implémentation des 9 Repositories :</b> Respect strict du package <code>tn.esprit.autoloc.repository</code> et de la convention <code>I...Repository</code>.", bullet_style))
    story.append(Paragraph("• <b>Maîtrise du cycle de vie CRUD :</b> Fonctionnement de <code>save()</code> (persist vs merge), <code>findById()</code> (Optional) et <code>deleteById()</code>.", bullet_style))
    story.append(Paragraph("• <b>Contrôle Qualité & Clean Code :</b> Analyse et correction d'anomalies critiques via <b>SonarQube for IDE (SonarLint)</b>.", bullet_style))

    # ==================== 2. RAPPELS THÉORIQUES ====================
    story.append(Spacer(1, 10))
    story.append(Paragraph("2. Rappels Théoriques Approfondis : De JPA à Spring Data JPA", h1_style))
    story.append(Paragraph(
        "Pour bien appréhender Spring Data JPA, il est fondamental de distinguer les trois niveaux de la pile de persistance Java :",
        body_style
    ))

    # Table Triplet JPA/Hibernate/Spring Data
    stack_data = [
        [Paragraph("Élément", tbl_header_style), Paragraph("Nature", tbl_header_style), Paragraph("Rôle & Responsabilité", tbl_header_style)],
        [
            Paragraph("<b>JPA</b><br/>(<code>jakarta.persistence</code>)", tbl_cell_bold),
            Paragraph("Spécification standard (JSR)", tbl_cell_style),
            Paragraph("Définit les annotations fondamentales (<code>@Entity</code>, <code>@Id</code>, <code>@OneToMany</code>, etc.) et l'interface standard <code>EntityManager</code>. N'exécute aucun code par lui-même.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Hibernate</b>", tbl_cell_bold),
            Paragraph("Implémentation ORM", tbl_cell_style),
            Paragraph("Implémente la spécification JPA. Traduit les objets Java en requêtes SQL adaptées au SGBD (MySQL), gère le contexte de persistance (cache L1), le <i>Dirty Checking</i> et les transactions.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Spring Data JPA</b>", tbl_cell_bold),
            Paragraph("Abstraction Spring", tbl_cell_style),
            Paragraph("Couche de haut niveau au-dessus de JPA/Hibernate. À partir d'une simple déclaration d'interface, Spring génère à l'exécution un <b>proxy dynamique</b> implémentant toutes les requêtes CRUD. <b>Supprime totalement l'écriture manuelle de classes DAO / RepositoryImpl.</b>", tbl_cell_style)
        ],
    ]
    stack_table = Table(stack_data, colWidths=[4 * cm, 4.2 * cm, 9.2 * cm])
    stack_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), c_primary),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E1")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, c_light_bg]),
        ('PADDING', (0,0), (-1,-1), 6),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
    ]))
    story.append(stack_table)
    story.append(Spacer(1, 10))

    # ==================== HIÉRARCHIE DES INTERFACES ====================
    story.append(Paragraph("2.1. Hiérarchie des Interfaces Spring Data JPA", h2_style))
    story.append(Paragraph(
        "Spring Data propose une hiérarchie modulaire d'interfaces génériques paramétrées par <code>&lt;T, ID&gt;</code> "
        "(où <code>T</code> est la classe de l'entité et <code>ID</code> le type wrapper de sa clé primaire, ex: <code>Long</code>) :",
        body_style
    ))

    # Callout Code Box for Interface Hierarchy
    hierarchy_code = """Repository<T, ID>                     (Interface marqueur vide, aucun contrat)
├── CrudRepository<T, ID>              (Opérations CRUD de base, renvoie Iterable<T>)
│   └── ListCrudRepository<T, ID>      (CRUD avec méthodes retournant des List<T>)
└── PagingAndSortingRepository<T, ID>  (Tri & Pagination, renvoie Iterable<T>)
    └── ListPagingAndSortingRepository (Tri & Pagination renvoyant des List<T>)

JpaRepository<T, ID> = ListCrudRepository + ListPagingAndSortingRepository
                      + QueryByExampleExecutor + Méthodes JPA (flush, saveAndFlush, batch)"""
    
    hier_table = Table([[Paragraph(hierarchy_code.replace("\n", "<br/>").replace(" ", "&nbsp;"), code_style)]], colWidths=[17.4 * cm])
    hier_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), c_code_bg),
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#334155")),
        ('PADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(hier_table)
    story.append(Spacer(1, 8))

    # Warning Box : Spring Data 3 changes
    warn_data = [[
        Paragraph("<b>Évolution majeure Spring Data 3 / Spring Boot 3 :</b><br/>"
                  "Historiquement, <code>PagingAndSortingRepository</code> héritait de <code>CrudRepository</code>. "
                  "Depuis Spring Data 3, ces deux branches sont <b>séparées</b> pour permettre une meilleure modularité. "
                  "L'interface <code>JpaRepository</code> combine désormais <code>ListCrudRepository</code> et <code>ListPagingAndSortingRepository</code>, "
                  "ce qui en fait l'interface la plus polyvalente et la plus adaptée pour les applications d'entreprise comme AutoLoc.", callout_style)
    ]]
    warn_table = Table(warn_data, colWidths=[17.4 * cm])
    warn_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), c_warn_bg),
        ('BOX', (0,0), (-1,-1), 1, c_warn_border),
        ('PADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(warn_table)

    story.append(PageBreak())

    # ==================== 2.2. DÉTAILS DES OPÉRATIONS CRUD ====================
    story.append(Paragraph("2.2. Mécanisme Détaillé des Opérations CRUD et Cycle de Vie JPA", h1_style))
    story.append(Paragraph(
        "L'utilisation correcte de Spring Data JPA nécessite de comprendre ce qui se passe sous le capot lors de chaque appel de méthode :",
        body_style
    ))

    # Table Operations
    ops_data = [
        [Paragraph("Méthode", tbl_header_style), Paragraph("Rôle & Type Retour", tbl_header_style), Paragraph("Comportement Hibernate & SQL sous-jacent", tbl_header_style)],
        [
            Paragraph("<b><code>save(entity)</code></b>", tbl_cell_bold),
            Paragraph("<code>&lt;S extends T&gt; S</code>", tbl_cell_style),
            Paragraph(
                "• <b>Si <code>id == null</code> (nouvelle entité) :</b> Déclenche <code>persist()</code>. Exécute un <code>INSERT</code> SQL. MySQL génère la clé primaire auto-incrémentée qui est injectée dans l'objet retourné.<br/>"
                "• <b>Si <code>id != null</code> (entité existante) :</b> Déclenche <code>merge()</code>. Exécute un <code>SELECT</code> pour charger l'état en base, applique le Dirty Checking, puis émet un <code>UPDATE</code> SQL si nécessaire.<br/>"
                "<b>Règle d'or :</b> Toujours récupérer la variable de retour : <code>contrat = contratRepo.save(contrat);</code>", tbl_cell_style)
        ],
        [
            Paragraph("<b><code>findById(id)</code></b>", tbl_cell_bold),
            Paragraph("<code>Optional&lt;T&gt;</code>", tbl_cell_style),
            Paragraph(
                "Effectue un <code>SELECT ... WHERE id = ?</code>. L'utilisation de <code>Optional</code> évite les <code>NullPointerException</code> "
                "et s'intègre parfaitement avec <code>.orElseThrow(() -&gt; new ResourceNotFoundException(...))</code> dans la couche Service.", tbl_cell_style)
        ],
        [
            Paragraph("<b><code>deleteById(id)</code></b>", tbl_cell_bold),
            Paragraph("<code>void</code>", tbl_cell_style),
            Paragraph(
                "Charge d'abord l'entité dans le contexte de persistance (connaissance de son état), puis exécute le <code>DELETE</code>. "
                "<b>Avantage crucial :</b> Déclenche les mécanismes de <code>cascade = CascadeType.REMOVE</code> et <code>orphanRemoval = true</code>.", tbl_cell_style)
        ],
        [
            Paragraph("<b><code>saveAndFlush(entity)</code></b>", tbl_cell_bold),
            Paragraph("<code>&lt;S extends T&gt; S</code>", tbl_cell_style),
            Paragraph(
                "Effectue un <code>save()</code> suivi immédiatement d'un <code>flush()</code>. Force la synchronisation immédiate du cache Hibernate vers la base de données sans attendre la fin du commit de transaction.", tbl_cell_style)
        ],
        [
            Paragraph("<b><code>deleteAllInBatch()</code></b>", tbl_cell_bold),
            Paragraph("<code>void</code>", tbl_cell_style),
            Paragraph(
                "<b>ATTENTION :</b> Exécute un <code>DELETE FROM table</code> direct en une seule requête SQL. "
                "<b>Contourne complètement le contexte de persistance :</b> Aucune cascade ni suppression orpheline n'est appliquée. "
                "Risque d'échec par violation de contrainte d'intégrité (Foreign Key) si des entités enfants existent (ex: paiements liés à un contrat).", tbl_cell_style)
        ],
    ]
    ops_table = Table(ops_data, colWidths=[3.8 * cm, 4.2 * cm, 9.4 * cm])
    ops_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), c_primary),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E1")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, c_light_bg]),
        ('PADDING', (0,0), (-1,-1), 6),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
    ]))
    story.append(ops_table)
    story.append(Spacer(1, 12))

    # ==================== 3. MISE EN PLACE DE LA COUCHE REPOSITORY ====================
    story.append(Paragraph("3. Mise en Œuvre dans le Projet AutoLoc API", h1_style))
    story.append(Paragraph(
        "Conformément aux directives de l'atelier, neuf interfaces repository ont été créées dans le package "
        "<code>tn.esprit.autoloc.repository</code>. Chaque interface hérite de <code>JpaRepository&lt;Entite, Long&gt;</code> "
        "et respecte la convention de nommage avec le préfixe <code>I</code>.",
        body_style
    ))

    # Table 9 Repositories
    repo_data = [
        [Paragraph("Entité Métier (<code>domain</code>)", tbl_header_style), Paragraph("Interface Repository (<code>repository</code>)", tbl_header_style), Paragraph("Interface Étendue & Justification Technique", tbl_header_style)],
        [
            Paragraph("<b>Agence</b>", tbl_cell_bold),
            Paragraph("<code>IAgenceRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Agence, Long&gt;</code><br/>Gestion complète des agences, pagination pour la liste d'administration, requêtes de recherche par ville.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Employe</b>", tbl_cell_bold),
            Paragraph("<code>IEmployeRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Employe, Long&gt;</code><br/>Listing sous forme de <code>List&lt;Employe&gt;</code>, gestion des rôles (Agent, Gestionnaire) par agence.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Vehicule</b>", tbl_cell_bold),
            Paragraph("<code>IVehiculeRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Vehicule, Long&gt;</code><br/>Gestion du parc automobile, pagination du catalogue public, tri par tarif journalier (<code>Sort</code>).", tbl_cell_style)
        ],
        [
            Paragraph("<b>Equipement</b>", tbl_cell_bold),
            Paragraph("<code>IEquipementRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Equipement, Long&gt;</code><br/>Catalogue des options/équipements (GPS, siège bébé). Retour direct en <code>List</code>.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Client</b>", tbl_cell_bold),
            Paragraph("<code>IClientRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Client, Long&gt;</code><br/>Enregistrement des clients, recherche par email unique, pagination de l'annuaire client.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Reservation</b>", tbl_cell_bold),
            Paragraph("<code>IReservationRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Reservation, Long&gt;</code><br/>Suivi des plannings de réservation, filtrage par statut et date, manipulation sous forme de List.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Contrat</b><br/><i>(Entité Pilote)</i>", tbl_cell_bold),
            Paragraph("<code>IContratRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Contrat, Long&gt;</code><br/>Gère la génération des contrats. Accès aux méthodes <code>saveAndFlush()</code> et propagation de la cascade vers les paiements.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Paiement</b>", tbl_cell_bold),
            Paragraph("<code>IPaiementRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Paiement, Long&gt;</code><br/>Lecture et audit comptable direct des transactions financières. <i>(Les écritures/suppressions restent orchestrées par le Contrat parent)</i>.", tbl_cell_style)
        ],
        [
            Paragraph("<b>Maintenance</b>", tbl_cell_bold),
            Paragraph("<code>IMaintenanceRepository</code>", tbl_cell_style),
            Paragraph("<code>JpaRepository&lt;Maintenance, Long&gt;</code><br/>Historique et suivi des entretiens périodiques et réparations techniques des véhicules.", tbl_cell_style)
        ],
    ]
    repo_table = Table(repo_data, colWidths=[3.8 * cm, 4.4 * cm, 9.2 * cm])
    repo_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), c_primary),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E1")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, c_light_bg]),
        ('PADDING', (0,0), (-1,-1), 4.5),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
    ]))
    story.append(repo_table)

    story.append(PageBreak())

    # ==================== 4. ANALYSE QUALITÉ SONARQUBE ====================
    story.append(Paragraph("4. Analyse Qualité de Code avec SonarQube for IDE (SonarLint)", h1_style))
    story.append(Paragraph(
        "L'intégration d'un outil d'analyse statique comme <b>SonarQube for IDE (SonarLint)</b> dans l'environnement "
        "de développement permet de détecter les mauvaises pratiques (code smells), les bugs potentiels et les failles "
        "de sécurité dès la phase d'écriture du code.",
        body_style
    ))

    story.append(Paragraph("<b>Tableau des anomalies relevées, règles associées et corrections apportées :</b>", body_style))

    # Table SonarLint
    sonar_data = [
        [Paragraph("#", tbl_header_style), Paragraph("Anomalie Détectée", tbl_header_style), Paragraph("Règle SonarLint & Justification Technique", tbl_header_style), Paragraph("Correction Appliquée", tbl_header_style)],
        [
            Paragraph("<b>1</b>", tbl_cell_bold),
            Paragraph("<b>Usage de l'annotation <code>@Data</code> sur les entités JPA</b>", tbl_cell_bold),
            Paragraph(
                "<b>Règle <code>java:S2160</code> / Danger Lombok JPA :</b><br/>"
                "<code>@Data</code> génère automatiquement un <code>equals()</code>, <code>hashCode()</code> et <code>toString()</code> incluant tous les champs.<br/>"
                "• <b>Boucle infinie :</b> Sur les relations bidirectionnelles (ex: <code>Contrat</code> &lt;-&gt; <code>Paiement</code>), l'appel à <code>toString()</code> provoque un <b><code>StackOverflowError</code></b>.<br/>"
                "• <b>Instabilité des Sets :</b> Si la clé primaire est générée lors du <code>persist</code>, le <code>hashCode()</code> change, cassant l'intégrité des collections <code>HashSet/Set</code>.", tbl_cell_style
            ),
            Paragraph("Remplacement systématique de <code>@Data</code> par des annotations explicites : <code>@Getter</code>, <code>@Setter</code>, <code>@NoArgsConstructor</code>, <code>@AllArgsConstructor</code>.", tbl_cell_style)
        ],
        [
            Paragraph("<b>2</b>", tbl_cell_bold),
            Paragraph("<b>Imports inutilisés et imports Wildcard</b>", tbl_cell_bold),
            Paragraph(
                "<b>Règle <code>java:S1128</code> (Unused imports) :</b><br/>"
                "La présence d'imports superflus ou de wildcards globaux (<code>import java.util.*;</code>) alourdit le code, peut masquer des conflits de noms de classes et dégrade la maintenabilité globale.", tbl_cell_style
            ),
            Paragraph("Suppression de tous les imports inutiles et réorganisation des imports sous forme explicite et ciblée.", tbl_cell_style)
        ],
        [
            Paragraph("<b>3</b>", tbl_cell_bold),
            Paragraph("<b>Non-respect du type d'identifiant générique</b>", tbl_cell_bold),
            Paragraph(
                "<b>Règles de généricité Java & Spécification Spring Data :</b><br/>"
                "L'utilisation d'un type primitif (ex: <code>long</code> au lieu de <code>Long</code>) dans les paramètres de type générique d'une interface provoque des erreurs ou warnings de compilation. De plus, les conventions de nommage imposent le préfixe <code>I</code> pour les interfaces de cet atelier.", tbl_cell_style
            ),
            Paragraph("Vérification et uniformisation de toutes les interfaces sous la forme <code>JpaRepository&lt;Entite, Long&gt;</code>.", tbl_cell_style)
        ],
        [
            Paragraph("<b>4</b>", tbl_cell_bold),
            Paragraph("<b>Utilisation risquée des suppressions en lot (<code>deleteInBatch</code>)</b>", tbl_cell_bold),
            Paragraph(
                "<b>Intégrité Référentielle & Cycle de vie JPA :</b><br/>"
                "Les méthodes batch exécutent directement du SQL brut sans passer par l'<code>EntityManager</code>. Les options <code>CascadeType.REMOVE</code> et <code>orphanRemoval</code> sont ignorées, ce qui risque de laisser des enregistrements orphelins ou de lever des contraintes de clés étrangères.", tbl_cell_style
            ),
            Paragraph("Recommandation d'utiliser <code>deleteById()</code> dans la couche Service pour garantir l'exécution des cascades définies.", tbl_cell_style)
        ],
    ]
    sonar_table = Table(sonar_data, colWidths=[0.8 * cm, 3.8 * cm, 7.2 * cm, 5.6 * cm])
    sonar_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), c_primary),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E1")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, c_light_bg]),
        ('PADDING', (0,0), (-1,-1), 5),
        ('VALIGN', (0,0), (-1,-1), 'TOP'),
    ]))
    story.append(sonar_table)
    story.append(Spacer(1, 12))

    # ==================== 5. VALIDATION ET RÉSULTATS ====================
    story.append(Paragraph("5. Validation, Exécution et Interprétation des Logs", h1_style))
    story.append(Paragraph(
        "Lors du démarrage de l'application Spring Boot avec les neuf interfaces repository configurées, "
        "Spring Data analyse le classpath et initialise les proxies dynamiques :",
        body_style
    ))

    # Log Card Box
    log_sample = """[INFO] --- compiler:3.13.0:compile (default-compile) @ autoloc-api ---
[INFO] Recompiling the module because of changed source code.
[INFO] Compiling 24 source files with javac [debug parameters release 17] to target\\classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[main] o.s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
[main] o.s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 48 ms. <b>Found 9 JPA repository interfaces.</b>"""
    
    log_table = Table([[Paragraph(log_sample.replace("\n", "<br/>").replace(" ", "&nbsp;"), code_style)]], colWidths=[17.4 * cm])
    log_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,-1), c_code_bg),
        ('BOX', (0,0), (-1,-1), 1, colors.HexColor("#334155")),
        ('PADDING', (0,0), (-1,-1), 8),
    ]))
    story.append(log_table)
    story.append(Spacer(1, 8))

    story.append(Paragraph(
        "<b>Constatations :</b><br/>"
        "1. <b>Aucune ligne DDL n'est générée en plus :</b> Le schéma relationnel en base de données correspond exactement à celui établi à l'Atelier 2.<br/>"
        "2. <b>Détection automatique :</b> Le log <code>Found 9 JPA repository interfaces</code> confirme que les neuf interfaces ont été identifiées et associées à leurs proxys d'exécution respectifs.<br/>"
        "3. <b>Livrable documentaire :</b> Le fichier <code>docs/repository-notes.md</code> a été rédigé et complété avec l'ensemble des justifications et résolutions de règles qualité.",
        body_style
    ))

    story.append(Spacer(1, 10))
    story.append(Paragraph("6. Synthèse des Livrables Attendus", h1_style))
    
    livrables_data = [
        [Paragraph("Livrable", tbl_header_style), Paragraph("Emplacement dans le projet", tbl_header_style), Paragraph("Statut", tbl_header_style)],
        [
            Paragraph("<b>9 Interfaces Repository</b>", tbl_cell_style),
            Paragraph("<code>src/main/java/tn/esprit/autoloc/repository/I*Repository.java</code>", tbl_cell_style),
            Paragraph("<font color='#0D9488'><b>CONFORME (9/9 créées & testées)</b></font>", tbl_cell_style)
        ],
        [
            Paragraph("<b>Note Documentaire Markdown</b>", tbl_cell_style),
            Paragraph("<code>docs/repository-notes.md</code>", tbl_cell_style),
            Paragraph("<font color='#0D9488'><b>CONFORME (Tableaux & Justifications)</b></font>", tbl_cell_style)
        ],
        [
            Paragraph("<b>Rapport Complet PDF</b>", tbl_cell_style),
            Paragraph("<code>Atelier_3_Spring_Data_JPA_Rapport.pdf</code>", tbl_cell_style),
            Paragraph("<font color='#0D9488'><b>GÉNÉRÉ & INTÉGRÉ</b></font>", tbl_cell_style)
        ],
    ]
    livrables_table = Table(livrables_data, colWidths=[5 * cm, 8 * cm, 4.4 * cm])
    livrables_table.setStyle(TableStyle([
        ('BACKGROUND', (0,0), (-1,0), c_primary),
        ('ALIGN', (0,0), (-1,0), 'CENTER'),
        ('GRID', (0,0), (-1,-1), 0.5, colors.HexColor("#CBD5E1")),
        ('ROWBACKGROUNDS', (0,1), (-1,-1), [colors.white, c_light_bg]),
        ('PADDING', (0,0), (-1,-1), 6),
        ('VALIGN', (0,0), (-1,-1), 'MIDDLE'),
    ]))
    story.append(livrables_table)

    # Build the document with two-pass canvas for header/footer page count
    doc.build(story, canvasmaker=NumberedCanvas)
    print(f"[OK] Document PDF généré avec succès : {filename}")

if __name__ == "__main__":
    create_pdf()
