DROP SCHEMA IF EXISTS `sistema_denuncias`;
CREATE SCHEMA IF NOT EXISTS `sistema_denuncias` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `sistema_denuncias`;

CREATE TABLE usuario (
	idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100),
    email VARCHAR(100),
    senha TEXT,
    UNIQUE (nome, email)
);

CREATE TABLE denuncia (
	idDenuncia INT AUTO_INCREMENT PRIMARY KEY,
    idCriador INT,
    titulo TEXT,
    categoria VARCHAR(50),
    descricao TEXT,
    dtDenuncia DATETIME,
    FOREIGN KEY (idCriador) REFERENCES usuario (idUsuario) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE comentario (
	idComentario INT PRIMARY KEY AUTO_INCREMENT,
    idUsuario INT,
    idDenuncia INT,
    texto TEXT,
    dtComentario DATETIME,
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE voto_prioridade (
	idUsuario INT,
    idDenuncia INT,
    valor_voto INT,
    PRIMARY KEY (idUsuario, idDenuncia),
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE voto_comentario (
    idUsuario INT,
    idComentario INT,
    PRIMARY KEY (idUsuario, idComentario),
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idComentario) REFERENCES comentario(idComentario) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE confirmacoes (
	idUsuario INT,
    idDenuncia INT,
    PRIMARY KEY (idUsuario, idDenuncia),
    FOREIGN KEY (idUsuario) REFERENCES usuario(idUsuario) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE midia (
	idMidia INT AUTO_INCREMENT PRIMARY KEY,
    idDenuncia INT,
    url TEXT,
    legenda TEXT,
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE endereco_fixo (
	idEndereco INT AUTO_INCREMENT PRIMARY KEY,
    idDenuncia INT,
    cep VARCHAR(10),
    rua TEXT,
    numero VARCHAR(10),
    bairro TEXT,
    cidade TEXT,
    estado CHAR(2),
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE coordenadas (
	idCoordenada INT AUTO_INCREMENT PRIMARY KEY,
    idDenuncia INT,
    latitude FLOAT,
    longitude FLOAT,
    cidade TEXT,
    estado CHAR(2),
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE ponto_referencia (
	idPonto INT AUTO_INCREMENT PRIMARY KEY,
    idDenuncia INT,
    nomePonto TEXT,
    descricaoPonto TEXT,
    cidade TEXT,
    estado CHAR(2),
    FOREIGN KEY (idDenuncia) REFERENCES denuncia(idDenuncia) ON DELETE CASCADE ON UPDATE CASCADE
);