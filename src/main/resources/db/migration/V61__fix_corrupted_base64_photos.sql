-- Corrige fotos de perfil corrompidas pelo sanitizador HTML (entidades &#43; &#61; &#47;)
UPDATE usuarios
SET foto_perfil = REPLACE(REPLACE(REPLACE(foto_perfil, '&#43;', '+'), '&#61;', '='), '&#47;', '/')
WHERE foto_perfil LIKE '%&#%';
