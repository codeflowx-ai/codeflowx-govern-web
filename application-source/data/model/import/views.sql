
 CREATE OR REPLACE VIEW CRMVETAPALEAD AS 
 SELECT aa.etapa,
    aa.idxcrmetapa,
    aa.mostrarmotiv,
    aa.orden,
    aa.porcentaje,
    aa.idcrmpipeline0,
    a.pipeline
   FROM (crmetapa aa
     JOIN crmpipeline a ON ((a.idxcrmpipeline = aa.idcrmpipeline0)))
  WHERE ((a.baja = false) AND (upper((a.modulo)::text) ~~ upper('%CRMOPORTUNIDAD%'::text)))
  ORDER BY aa.orden;;

 CREATE OR REPLACE VIEW CRMVLEADS AS 
 SELECT a.etapa,
    a.orden,
    aa.pipeline,
    ab.descripcion,
    ab.alta,
    ab.fectstart,
    ab.importeprev,
    ab.oportunidad,
    ab."position",
    ab.probabilidad,
    ab.idcrmpipeline0,
    ab.idcrmetapa0,
    ab.idxoportunidad
   FROM ((crmoportunidad ab
     JOIN crmetapa a ON ((a.idxcrmetapa = ab.idcrmetapa0)))
     JOIN crmpipeline aa ON ((aa.idxcrmpipeline = ab.idcrmpipeline0)))
  WHERE (ab.archived = false);;

 CREATE OR REPLACE VIEW CRMVPIPELINELEAD AS 
 SELECT a.baja,
    a.defecto,
    a.idxcrmpipeline,
    a.maxreg,
    a.modulo,
    a.ordenasc,
    a.pipeline
   FROM crmpipeline a
  WHERE ((a.baja = false) AND (upper((a.modulo)::text) ~~ upper('%crmoportunidad%'::text)));;

 CREATE OR REPLACE VIEW RRHHVDEFAULTCALENDAR AS 
 SELECT a.festivos,
    a.defecto,
    a.descripcion,
    a.domingo,
    a.jueves,
    a.lunes,
    a.martes,
    a.miercoles,
    a.sabado,
    a.nombre,
    a.viernes,
    aa.tipo,
    aa.fecha,
    aa.descripcin,
    aa.idxrrhhfestivos,
    aa.idrrhhcalendario0
   FROM (rrhhfestivos aa
     JOIN rrhhcalendario a ON ((a.idxrrhhcalendario = aa.idrrhhcalendario0)))
  WHERE (a.defecto = true);;

 CREATE OR REPLACE VIEW SSOVPERMROL AS 
 SELECT a.aplicacion,
    a.idxaplicacion,
    aa.icono,
    aa.idxssomenu,
    aa.menu,
    ab.idxssomenuitem,
    ab.item,
    ab.namespace,
    ab.page,
    ab.url,
    ac.rol,
    ac.dashboard
   FROM ((((ssomenuitem ab
     JOIN ssomenu aa ON ((aa.idxssomenu = ab.idssomenu0)))
     JOIN ssorolesmenu ad ON ((ad.idssomenuitem0 = ab.idxssomenuitem)))
     JOIN ssorol ac ON ((ac.idxssorol = ad.idssorol0)))
     JOIN ssoaplicacion a ON ((a.idxaplicacion = aa.idssoaplicacion0)));;

 CREATE OR REPLACE VIEW VRRHCOUNTFICHAJE AS 
 SELECT sum(rhfichar.total) AS total,
    rhfichar.tipo,
    date_part('month'::text, rhfichar.fecha) AS mes,
    date_part('year'::text, rhfichar.fecha) AS anio,
    rhfichar.aprobada,
    rhfichar.idrrhempleado0
   FROM rhfichar
  GROUP BY (date_part('month'::text, rhfichar.fecha)), (date_part('year'::text, rhfichar.fecha)), rhfichar.tipo, rhfichar.aprobada, rhfichar.idrrhempleado0;;
