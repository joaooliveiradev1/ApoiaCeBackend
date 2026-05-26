-- MySQL dump 10.13  Distrib 8.0.45, for Win64 (x86_64)
--
-- Host: localhost    Database: apoiace
-- ------------------------------------------------------
-- Server version	8.0.45

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `assinaturas`
--

DROP TABLE IF EXISTS `assinaturas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `assinaturas` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `assinante_id` char(36) NOT NULL,
  `projeto_id` char(36) NOT NULL,
  `valor` decimal(12,2) NOT NULL,
  `anonima` tinyint(1) NOT NULL DEFAULT '0',
  `status` enum('ATIVA','CANCELADA','INADIMPLENTE') NOT NULL DEFAULT 'ATIVA',
  `recorrente` tinyint(1) NOT NULL DEFAULT '1',
  `inicio_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `cancelada_em` datetime(3) DEFAULT NULL,
  `proxima_cobranca_em` date DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_assinaturas_assinante_id` (`assinante_id`),
  KEY `idx_assinaturas_projeto_id` (`projeto_id`),
  CONSTRAINT `fk_assinaturas_projeto` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id`),
  CONSTRAINT `FKhyw14f5fkwc1aka9bb81fsc57` FOREIGN KEY (`assinante_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `atualizacoes`
--

DROP TABLE IF EXISTS `atualizacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `atualizacoes` (
  `id` varchar(255) NOT NULL,
  `projeto_id` char(36) NOT NULL,
  `titulo` varchar(160) NOT NULL,
  `conteudo_publico` text,
  `conteudo_exclusivo` text,
  `exclusiva` tinyint(1) NOT NULL DEFAULT '0',
  `publicada_em` datetime(3) DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_atualizacoes_projeto_id` (`projeto_id`),
  CONSTRAINT `fk_atualizacoes_projeto` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `categorias`
--

DROP TABLE IF EXISTS `categorias`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `categorias` (
  `id` char(36) NOT NULL,
  `nome` varchar(80) NOT NULL,
  `cor` varchar(20) DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_categorias_nome` (`nome`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `conteudos_projeto`
--

DROP TABLE IF EXISTS `conteudos_projeto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `conteudos_projeto` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `projeto_id` char(36) NOT NULL,
  `tipo` enum('TEXTO','IMAGEM','VIDEO','LINK','ARQUIVO') NOT NULL DEFAULT 'TEXTO',
  `conteudo` text NOT NULL,
  `posicao` int NOT NULL DEFAULT '0',
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_conteudos_projeto_projeto_id` (`projeto_id`),
  CONSTRAINT `fk_conteudos_projeto_projeto` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enquetes`
--

DROP TABLE IF EXISTS `enquetes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enquetes` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `projeto_id` char(36) NOT NULL,
  `titulo` varchar(160) NOT NULL,
  `descricao` text,
  `ativa` tinyint(1) NOT NULL DEFAULT '1',
  `data_criacao` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_enquetes_projeto_id` (`projeto_id`),
  CONSTRAINT `fk_enquetes_projeto` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notificacoes`
--

DROP TABLE IF EXISTS `notificacoes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacoes` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `usuario_id` char(36) NOT NULL,
  `projeto_id` char(36) DEFAULT NULL,
  `titulo` varchar(160) NOT NULL,
  `mensagem` text NOT NULL,
  `tipo` enum('GERAL','PAGAMENTO','ASSINATURA','ENQUETE','PROJETO') NOT NULL DEFAULT 'GERAL',
  `lida` tinyint(1) NOT NULL DEFAULT '0',
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_notificacoes_usuario_id` (`usuario_id`),
  KEY `idx_notificacoes_projeto_id` (`projeto_id`),
  CONSTRAINT `fk_notificacoes_projeto` FOREIGN KEY (`projeto_id`) REFERENCES `projetos` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `opcoes_enquete`
--

DROP TABLE IF EXISTS `opcoes_enquete`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `opcoes_enquete` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `enquete_id` char(36) NOT NULL,
  `titulo` varchar(160) NOT NULL,
  `qtd_votos` int NOT NULL DEFAULT '0',
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_opcoes_enquete_enquete_id` (`enquete_id`),
  CONSTRAINT `fk_opcoes_enquete_enquete` FOREIGN KEY (`enquete_id`) REFERENCES `enquetes` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `pagamentos`
--

DROP TABLE IF EXISTS `pagamentos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `pagamentos` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `assinatura_id` char(36) NOT NULL,
  `gateway_tx_id` varchar(120) NOT NULL,
  `valor_pago` decimal(12,2) DEFAULT '0.00',
  `status` enum('PENDENTE','CONFIRMADO','FALHOU','CANCELADO','ESTORNADO','EXPIRADO') NOT NULL,
  `meio_pagamento` enum('PIX','CARTAO_CREDITO','BOLETO') NOT NULL,
  `taxa_plataforma` decimal(12,2) DEFAULT '0.00',
  `valor_liquido` decimal(12,2) DEFAULT '0.00',
  `parcelas` int NOT NULL DEFAULT '1',
  `competencia` date DEFAULT NULL,
  `data_pagamento` datetime(3) DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_pagamentos_gateway_tx_id` (`gateway_tx_id`),
  KEY `idx_pagamentos_assinatura_id` (`assinatura_id`),
  KEY `idx_pagamentos_status` (`status`),
  CONSTRAINT `fk_pagamentos_assinatura` FOREIGN KEY (`assinatura_id`) REFERENCES `assinaturas` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `password_reset_tokens`
--

DROP TABLE IF EXISTS `password_reset_tokens`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_tokens` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `token` varchar(120) NOT NULL,
  `usuario_id` char(36) NOT NULL,
  `expires_at` datetime(3) NOT NULL,
  `used_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_password_reset_token` (`token`),
  KEY `idx_password_reset_usuario_id` (`usuario_id`),
  CONSTRAINT `fk_password_reset_tokens_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `perfis_usuario`
--

DROP TABLE IF EXISTS `perfis_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `perfis_usuario` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `usuario_id` char(36) NOT NULL,
  `bio` text,
  `pix_chave` varchar(120) DEFAULT NULL,
  `conta_bancaria` json DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_perfis_usuario_usuario_id` (`usuario_id`),
  CONSTRAINT `fk_perfis_usuario_usuario` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `projetos`
--

DROP TABLE IF EXISTS `projetos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `projetos` (
  `id` char(36) NOT NULL,
  `titulo` varchar(160) NOT NULL,
  `slug` varchar(180) NOT NULL,
  `descricao` text,
  `meta_valor` decimal(12,2) NOT NULL DEFAULT '0.00',
  `valor_captado` decimal(12,2) NOT NULL DEFAULT '0.00',
  `qtd_apoiadores` int NOT NULL DEFAULT '0',
  `data_fim` date DEFAULT NULL,
  `tipo_assinatura` enum('MENSAL','UNICA') NOT NULL DEFAULT 'MENSAL',
  `status` enum('RASCUNHO','PUBLICADO','PAUSADO','ENCERRADO') NOT NULL DEFAULT 'RASCUNHO',
  `criador_id` char(36) NOT NULL,
  `categoria_id` char(36) NOT NULL,
  `video_url` text,
  `capa_url` text,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_projetos_slug` (`slug`),
  KEY `idx_projetos_criador_id` (`criador_id`),
  KEY `idx_projetos_categoria_id` (`categoria_id`),
  CONSTRAINT `fk_projeto_categoria` FOREIGN KEY (`categoria_id`) REFERENCES `categorias` (`id`),
  CONSTRAINT `fk_projeto_criador` FOREIGN KEY (`criador_id`) REFERENCES `usuarios` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` char(36) NOT NULL,
  `nome` varchar(120) NOT NULL,
  `email` varchar(190) NOT NULL,
  `senha_hash` varchar(255) NOT NULL,
  `cpf` varchar(11) DEFAULT NULL,
  `role` enum('ADMIN','APOIADOR','CRIADOR') NOT NULL,
  `telefone` varchar(20) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_usuarios_email` (`email`),
  UNIQUE KEY `uk_usuarios_cpf` (`cpf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `votos`
--

DROP TABLE IF EXISTS `votos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `votos` (
  `id` char(36) NOT NULL DEFAULT (uuid()),
  `opcao_id` char(36) NOT NULL,
  `usuario_id` char(36) NOT NULL,
  `data_voto` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `criado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `atualizado_em` datetime(3) NOT NULL DEFAULT CURRENT_TIMESTAMP(3),
  `deleted_at` datetime(3) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_votos_opcao_id` (`opcao_id`),
  KEY `idx_votos_usuario_id` (`usuario_id`),
  CONSTRAINT `fk_votos_opcao` FOREIGN KEY (`opcao_id`) REFERENCES `opcoes_enquete` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-05-16 17:25:20
