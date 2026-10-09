UPDATE usuarios SET foto_perfil = NULL WHERE LENGTH(foto_perfil) > 1048576;
