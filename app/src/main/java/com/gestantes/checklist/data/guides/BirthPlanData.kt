package com.gestantes.checklist.data.guides

import com.gestantes.checklist.data.entity.BirthPlanItem

/**
 * Dados pré-definidos para o Plano de Parto
 */
object BirthPlanData {
    
    fun getDefaultItems(): List<BirthPlanItem> = listOf(
        // AMBIENTE
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Luz ambiente baixa",
            description = "Preferência por iluminação suave e aconchegante",
            order = 1
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Música ambiente",
            description = "Tocar playlist própria ou música relaxante",
            order = 2
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Aromaterapia",
            description = "Uso de óleos essenciais para relaxamento",
            order = 3
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Livre movimentação",
            description = "Poder caminhar e mudar de posição livremente",
            order = 4
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Presença de acompanhante",
            description = "Ter uma pessoa de confiança durante todo o processo",
            order = 5
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Presença de doula",
            description = "Ter suporte de uma doula profissional",
            order = 6
        ),
        BirthPlanItem(
            category = "AMBIENTE",
            title = "Privacidade",
            description = "Limitar entrada de pessoas no ambiente",
            order = 7
        ),
        
        // TRABALHO DE PARTO
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Uso de bola de pilates",
            description = "Para auxiliar no trabalho de parto",
            order = 1
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Banho quente/banheira",
            description = "Uso de água para alívio da dor",
            order = 2
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Massagem",
            description = "Receber massagens durante as contrações",
            order = 3
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Evitar induções desnecessárias",
            description = "Preferência por aguardar o trabalho de parto natural",
            order = 4
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Monitoramento intermitente",
            description = "Evitar monitoramento fetal contínuo se não necessário",
            order = 5
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Alimentação leve",
            description = "Poder ingerir líquidos e alimentos leves",
            order = 6
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Evitar ocitocina sintética",
            description = "Preferência por não usar medicamentos para acelerar",
            order = 7
        ),
        BirthPlanItem(
            category = "TRABALHO_PARTO",
            title = "Analgesia/Anestesia",
            description = "Opção por anestesia epidural se necessário",
            order = 8
        ),
        
        // PARTO
        BirthPlanItem(
            category = "PARTO",
            title = "Parto normal",
            description = "Preferência por parto vaginal",
            order = 1
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Posição de escolha",
            description = "Parir na posição mais confortável (cócoras, lateral, etc.)",
            order = 2
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Evitar episiotomia",
            description = "Preferência por não realizar corte no períneo",
            order = 3
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Esperar expulsão natural da placenta",
            description = "Aguardar a placenta sair naturalmente",
            order = 4
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Clampeamento tardio do cordão",
            description = "Aguardar o cordão parar de pulsar antes de cortar",
            order = 5
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Acompanhante corta o cordão",
            description = "Permitir que o pai/acompanhante corte o cordão",
            order = 6
        ),
        BirthPlanItem(
            category = "PARTO",
            title = "Fotografar/filmar o parto",
            description = "Registrar o momento do nascimento",
            order = 7
        ),
        
        // CUIDADOS COM O BEBÊ
        BirthPlanItem(
            category = "BEBE",
            title = "Contato pele a pele imediato",
            description = "Colocar o bebê no peito logo após o nascimento",
            order = 1
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Amamentação na primeira hora",
            description = "Iniciar amamentação logo após o parto",
            order = 2
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Evitar aspiração de rotina",
            description = "Só aspirar se realmente necessário",
            order = 3
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Adiar primeiro banho",
            description = "Manter o vérnix protetor nas primeiras horas",
            order = 4
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Vacinas e vitamina K",
            description = "Seguir o calendário de vacinas e aplicar vitamina K",
            order = 5
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Teste do pezinho",
            description = "Realizar exame de triagem neonatal",
            order = 6
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Alojamento conjunto",
            description = "Bebê ficar 24h no quarto com a mãe",
            order = 7
        ),
        BirthPlanItem(
            category = "BEBE",
            title = "Evitar mamadeiras e chupetas",
            description = "Não oferecer bicos artificiais",
            order = 8
        ),
        
        // PÓS-PARTO
        BirthPlanItem(
            category = "POS_PARTO",
            title = "Apoio à amamentação",
            description = "Ter suporte de consultora em amamentação",
            order = 1
        ),
        BirthPlanItem(
            category = "POS_PARTO",
            title = "Visitas limitadas",
            description = "Restringir visitas nas primeiras horas/dias",
            order = 2
        ),
        BirthPlanItem(
            category = "POS_PARTO",
            title = "Alta precoce",
            description = "Preferência por alta assim que possível",
            order = 3
        ),
        BirthPlanItem(
            category = "POS_PARTO",
            title = "Suporte emocional",
            description = "Ter acompanhamento psicológico se necessário",
            order = 4
        ),
        BirthPlanItem(
            category = "POS_PARTO",
            title = "Orientações sobre cuidados",
            description = "Receber instruções sobre cuidados com bebê e recuperação",
            order = 5
        )
    )
}
