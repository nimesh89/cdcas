--SELECT AddGeometryColumn('','provincemap','the_geom','-1','MULTIPOLYGON',2);
--delete from districmap
--insert into districmap (dcode, province_n, dist, the_geom)
--SELECT dcode, province_n,  dist, ST_AsText(multi(ST_Union(the_geom))) as the_geom
--FROM divsec
--group by dcode, province_n,  dist

insert into provincemap (province_c, province_n, the_geom)
SELECT province_c, province_n, ST_AsText(multi(ST_Union(the_geom))) as the_geom
FROM districmap
group by province_c, province_n