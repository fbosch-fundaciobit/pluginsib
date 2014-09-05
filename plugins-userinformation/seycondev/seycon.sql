--
-- PostgreSQL database dump
--

-- Started on 2013-12-13 11:18:06

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = off;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET escape_string_warning = off;

SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 140 (class 1259 OID 179470)
-- Dependencies: 5
-- Name: sc_wl_certif; Type: TABLE; Schema: public; Owner: seycon; Tablespace: 
--

CREATE TABLE sc_wl_certif (
    cer_codusu character varying(30),
    cer_emisor character varying(1024),
    cer_serial character varying(128)
);


ALTER TABLE public.sc_wl_certif OWNER TO seycon;

--
-- TOC entry 141 (class 1259 OID 179476)
-- Dependencies: 5
-- Name: sc_wl_logaut; Type: TABLE; Schema: public; Owner: seycon; Tablespace: 
--

CREATE TABLE sc_wl_logaut (
    log_dat date NOT NULL,
    log_tipo character(1) NOT NULL,
    log_login character varying(15) NOT NULL,
    log_codusu character varying(250),
    log_resul character(1) NOT NULL,
    log_certif character varying(4000),
    log_data date,
    log_ip character varying(240)
);


ALTER TABLE public.sc_wl_logaut OWNER TO seycon;

--
-- TOC entry 142 (class 1259 OID 179482)
-- Dependencies: 5
-- Name: sc_wl_usuari; Type: TABLE; Schema: public; Owner: seycon; Tablespace: 
--

CREATE TABLE sc_wl_usuari (
    usu_codi character varying(512) NOT NULL,
    usu_pass character varying(255),
    usu_datcad date,
    usu_nom character varying(200),
    usu_nif character varying(15)
);


ALTER TABLE public.sc_wl_usuari OWNER TO seycon;

--
-- TOC entry 143 (class 1259 OID 179488)
-- Dependencies: 5
-- Name: sc_wl_usugru; Type: TABLE; Schema: public; Owner: seycon; Tablespace: 
--

CREATE TABLE sc_wl_usugru (
    ugr_codusu character varying(512) NOT NULL,
    ugr_codgru character varying(50) NOT NULL
);


ALTER TABLE public.sc_wl_usugru OWNER TO seycon;

--
-- TOC entry 1792 (class 0 OID 179470)
-- Dependencies: 140
-- Data for Name: sc_wl_certif; Type: TABLE DATA; Schema: public; Owner: seycon
--



--
-- TOC entry 1793 (class 0 OID 179476)
-- Dependencies: 141
-- Data for Name: sc_wl_logaut; Type: TABLE DATA; Schema: public; Owner: seycon
--



--
-- TOC entry 1794 (class 0 OID 179482)
-- Dependencies: 142
-- Data for Name: sc_wl_usuari; Type: TABLE DATA; Schema: public; Owner: seycon
--

INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('admin', 'admin', NULL, 'admin', '12345678Z');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('aden2', 'aden2', NULL, 'Admin Entitat deCaib', '00000002E');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('provamarilen', 'provamarilen', NULL, 'Marilen Prova', '11222333D');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('senserol', 'senserol', NULL, 'Sense Rol', '11111111A');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('dest2', 'dest2', NULL, 'Destinatari2', '00000002T');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('soli1', 'soli1', NULL, 'Solicitant1', '00000001S');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('dest1', 'dest1', NULL, 'Destinatari1', '00000001T');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('dboerner', 'dboerner', NULL, 'Daniel Boerner', 'X0468112Q');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('marilenprova2', 'marilenprova2', NULL, 'marilen prova 2', '22222222M');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('dele2', 'dele2', NULL, 'Delegat 2', '00000002D');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('dele1', 'dele1', NULL, 'Delegat 1', '00000001D');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('cola1', 'cola1', NULL, 'Colaborador 1', '00000001C');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('aden1', 'aden1', NULL, 'Admin Entitat de FundacioBit', '00000001E');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('cola2', 'cola2', NULL, 'Colaborador 2', '00000002C');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('soli2', 'soli2', NULL, 'Solicitant2', '00000002S');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('anadal', 'anadal', NULL, 'Antoni Nadal', '43096845C');
INSERT INTO sc_wl_usuari (usu_codi, usu_pass, usu_datcad, usu_nom, usu_nif) VALUES ('marilen', 'marilen', NULL, 'Marilen', '44328254D');


--
-- TOC entry 1795 (class 0 OID 179488)
-- Dependencies: 143
-- Data for Name: sc_wl_usugru; Type: TABLE DATA; Schema: public; Owner: seycon
--

INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('aden1', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('aden2', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('admin', 'PFI_ADMIN');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('anadal', 'PFI_ADMIN');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('anadal', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('anadaluser', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('cola1', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('cola2', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dboerner', 'PFI_ADMIN');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dboerner', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dele1', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dele2', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dest1', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('dest2', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('marilen', 'PFI_ADMIN');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('marilen', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('marilenprova2', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('provamarilen', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('senserol', 'PFI_ADMIN');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('soli1', 'PFI_USER');
INSERT INTO sc_wl_usugru (ugr_codusu, ugr_codgru) VALUES ('soli2', 'PFI_USER');


--
-- TOC entry 1790 (class 2606 OID 179497)
-- Dependencies: 143 143 143
-- Name: ugr_wl_pk; Type: CONSTRAINT; Schema: public; Owner: seycon; Tablespace: 
--

ALTER TABLE ONLY sc_wl_usugru
    ADD CONSTRAINT ugr_wl_pk PRIMARY KEY (ugr_codusu, ugr_codgru);


--
-- TOC entry 1787 (class 2606 OID 179499)
-- Dependencies: 142 142
-- Name: usu_wl_pk; Type: CONSTRAINT; Schema: public; Owner: seycon; Tablespace: 
--

ALTER TABLE ONLY sc_wl_usuari
    ADD CONSTRAINT usu_wl_pk PRIMARY KEY (usu_codi);


--
-- TOC entry 1791 (class 1259 OID 179495)
-- Dependencies: 143 143
-- Name: ugr_wl_usugru_pk; Type: INDEX; Schema: public; Owner: seycon; Tablespace: 
--

CREATE UNIQUE INDEX ugr_wl_usugru_pk ON sc_wl_usugru USING btree (ugr_codusu, ugr_codgru);


--
-- TOC entry 1788 (class 1259 OID 179494)
-- Dependencies: 142
-- Name: usu_wl_usuari_pk; Type: INDEX; Schema: public; Owner: seycon; Tablespace: 
--

CREATE UNIQUE INDEX usu_wl_usuari_pk ON sc_wl_usuari USING btree (usu_codi);


--
-- TOC entry 1799 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: seycon
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM seycon;
GRANT ALL ON SCHEMA public TO seycon;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-12-13 11:18:07

--
-- PostgreSQL database dump complete
--

