package com.gestantes.checklist.data.guides

/**
 * Guia completo de sono para bebês baseado em pesquisas pediátricas atualizadas.
 * 
 * IMPORTANTE: Este guia não substitui o acompanhamento médico.
 * Sempre consulte o pediatra do seu bebê para orientações personalizadas.
 */
object SleepGuideData {

    data class SleepInfo(
        val ageRange: String,
        val totalSleepHours: String,
        val nightSleepHours: String,
        val napInfo: String,
        val wakeWindow: String,
        val tips: List<String>,
        val challenges: List<String>
    )

    val sleepByAge = listOf(
        SleepInfo(
            ageRange = "0-1 mês (Recém-nascido)",
            totalSleepHours = "16-18 horas",
            nightSleepHours = "8-9 horas (com despertares)",
            napInfo = "7-8 sonecas curtas ao longo do dia",
            wakeWindow = "45 min - 1 hora",
            tips = listOf(
                "Bebês não distinguem dia e noite ainda - é normal!",
                "Alimente sempre que o bebê pedir (livre demanda)",
                "Mantenha o ambiente claro durante o dia e escuro à noite",
                "Não se preocupe com rotina rígida agora",
                "O sono é fragmentado e isso é esperado",
                "Durma quando o bebê dormir para se recuperar"
            ),
            challenges = listOf(
                "Trocar o dia pela noite é muito comum",
                "Despertares frequentes para mamar (a cada 2-3h)",
                "Reflexo de Moro pode acordar o bebê",
                "Cólicas podem aparecer ao final do primeiro mês"
            )
        ),
        SleepInfo(
            ageRange = "1-2 meses",
            totalSleepHours = "15-17 horas",
            nightSleepHours = "8-10 horas (com despertares)",
            napInfo = "4-5 sonecas por dia",
            wakeWindow = "1 hora - 1h15",
            tips = listOf(
                "Comece a criar pequenos rituais de sono",
                "Banho morno antes de dormir ajuda a relaxar",
                "Use swaddle (enrolar) se o bebê gosta",
                "Ruído branco pode ajudar muito",
                "Observe os sinais de sono: bocejos, olhos vermelhos",
                "Exponha o bebê à luz natural durante o dia"
            ),
            challenges = listOf(
                "Pico de cólicas (geralmente entre 4-6 semanas)",
                "Confusão dia/noite pode persistir",
                "Bebê ainda não produz melatonina própria",
                "Período de maior choro do desenvolvimento"
            )
        ),
        SleepInfo(
            ageRange = "2-3 meses",
            totalSleepHours = "14-16 horas",
            nightSleepHours = "9-10 horas (com 1-2 despertares)",
            napInfo = "3-4 sonecas por dia",
            wakeWindow = "1h15 - 1h30",
            tips = listOf(
                "Corpo começa a produzir melatonina - aproveite!",
                "Estabeleça uma rotina noturna consistente",
                "Coloque para dormir entre 19h-20h",
                "Última soneca não deve passar das 17h",
                "Ambiente deve ficar mais escuro a partir das 18h",
                "Massagem relaxante pode ajudar"
            ),
            challenges = listOf(
                "Cólicas começam a melhorar",
                "Bebê pode resistir ao swaddle",
                "Padrões de sono ainda irregulares",
                "Pode haver confusão de fome com sono"
            )
        ),
        SleepInfo(
            ageRange = "3-4 meses",
            totalSleepHours = "14-15 horas",
            nightSleepHours = "10-11 horas (com despertares)",
            napInfo = "3-4 sonecas por dia",
            wakeWindow = "1h30 - 2 horas",
            tips = listOf(
                "REGRESSÃO DO SONO pode acontecer - é normal!",
                "Mantenha a rotina mesmo que pareça não funcionar",
                "Bebê começa a reconhecer padrões",
                "Considere transição do swaddle para saco de dormir",
                "Seja consistente nos horários",
                "Não crie novos hábitos durante a regressão"
            ),
            challenges = listOf(
                "Regressão dos 4 meses (mudança nos ciclos de sono)",
                "Bebê acorda mais do que antes - é temporário",
                "Pode rejeitar sonecas que antes fazia",
                "Maior consciência do ambiente"
            )
        ),
        SleepInfo(
            ageRange = "4-6 meses",
            totalSleepHours = "13-15 horas",
            nightSleepHours = "10-12 horas",
            napInfo = "2-3 sonecas por dia",
            wakeWindow = "2 - 2h30",
            tips = listOf(
                "Boa fase para trabalhar independência no sono",
                "Coloque o bebê sonolento mas acordado no berço",
                "Rotina de sono bem estabelecida",
                "Introdução alimentar pode começar (6 meses)",
                "Mantenha mamadas noturnas se necessário",
                "Horário de dormir entre 18h30-19h30"
            ),
            challenges = listOf(
                "Nascimento dos primeiros dentes pode atrapalhar",
                "Ansiedade de separação começa a surgir",
                "Interesse pelo ambiente pode dificultar sonecas",
                "Pode começar a rolar e acordar"
            )
        ),
        SleepInfo(
            ageRange = "6-9 meses",
            totalSleepHours = "12-14 horas",
            nightSleepHours = "10-12 horas",
            napInfo = "2 sonecas por dia (manhã e tarde)",
            wakeWindow = "2h30 - 3h30",
            tips = listOf(
                "Transição para 2 sonecas geralmente acontece",
                "Soneca da manhã: ~1,5h / Soneca da tarde: ~1,5h",
                "Alguns bebês já dormem a noite toda",
                "Mantenha alimentação adequada durante o dia",
                "Considere objeto de transição (naninha)",
                "Rotina previsível é essencial"
            ),
            challenges = listOf(
                "Ansiedade de separação mais intensa",
                "Dentição ativa",
                "Marcos motores podem atrapalhar (sentar, engatinhar)",
                "Regressão dos 8-9 meses pode ocorrer"
            )
        ),
        SleepInfo(
            ageRange = "9-12 meses",
            totalSleepHours = "12-14 horas",
            nightSleepHours = "11-12 horas",
            napInfo = "2 sonecas por dia",
            wakeWindow = "3 - 4 horas",
            tips = listOf(
                "Maioria dos bebês pode dormir a noite toda",
                "Soneca da manhã mais curta (~1h), tarde mais longa (~1,5-2h)",
                "Jantar consistente ajuda no sono noturno",
                "Mantenha objeto de conforto no berço",
                "Horário de dormir: 19h-20h",
                "Evite soneca da tarde muito tarde"
            ),
            challenges = listOf(
                "Pode tentar ficar de pé no berço",
                "Aprendendo a andar - quer praticar à noite",
                "Resistência na hora de dormir pode aumentar",
                "Pesadelos podem começar"
            )
        ),
        SleepInfo(
            ageRange = "12-18 meses",
            totalSleepHours = "12-14 horas",
            nightSleepHours = "11-12 horas",
            napInfo = "1-2 sonecas (transição para 1)",
            wakeWindow = "4 - 5 horas",
            tips = listOf(
                "Transição de 2 para 1 soneca acontece",
                "Soneca única: ~2-3 horas após o almoço",
                "Regressão dos 12 meses pode ocorrer",
                "Continue com rotina consistente",
                "Introduza livros e histórias antes de dormir",
                "Paciência com a transição de sonecas"
            ),
            challenges = listOf(
                "Regressão dos 12 meses",
                "Transição de sonecas pode causar irritabilidade",
                "Maior independência = mais resistência",
                "Pode querer dormir na cama dos pais"
            )
        ),
        SleepInfo(
            ageRange = "18-24 meses",
            totalSleepHours = "11-14 horas",
            nightSleepHours = "10-12 horas",
            napInfo = "1 soneca de 1,5-2,5 horas",
            wakeWindow = "5 - 6 horas",
            tips = listOf(
                "Regressão dos 18 meses é intensa - aguente firme!",
                "Soneca única após o almoço",
                "Comece a preparar para transição do berço (se necessário)",
                "Mantenha limites claros e amorosos",
                "Histórias e músicas ajudam na rotina",
                "Seja firme mas carinhoso nos limites"
            ),
            challenges = listOf(
                "Regressão dos 18 meses (uma das mais difíceis)",
                "Terrores noturnos podem começar",
                "Dentes molares nascendo",
                "Fase do 'não' - pode resistir a dormir"
            )
        ),
        SleepInfo(
            ageRange = "2-3 anos",
            totalSleepHours = "11-14 horas",
            nightSleepHours = "10-12 horas",
            napInfo = "1 soneca (pode começar a resistir)",
            wakeWindow = "5-7 horas",
            tips = listOf(
                "Transição para cama pode acontecer",
                "Mantenha soneca enquanto a criança precisar",
                "Relógio de sono pode ajudar (luz verde = pode levantar)",
                "Estabeleça regras claras sobre hora de dormir",
                "Permita escolhas: 'Qual pijama você quer?'",
                "Evite telas 2 horas antes de dormir"
            ),
            challenges = listOf(
                "Pode começar a sair da cama sozinho",
                "Pesadelos mais elaborados",
                "Medo do escuro pode surgir",
                "Pode querer abandonar a soneca cedo demais"
            )
        ),
        SleepInfo(
            ageRange = "3-4 anos",
            totalSleepHours = "10-13 horas",
            nightSleepHours = "10-12 horas",
            napInfo = "Soneca opcional (muitos param de fazer)",
            wakeWindow = "Todo o dia (se não fizer soneca)",
            tips = listOf(
                "Muitas crianças param de fazer soneca",
                "Se parar soneca, antecipe hora de dormir",
                "Substitua soneca por 'hora do descanso' quieta",
                "Continue com rotina de sono consistente",
                "Converse sobre medos e pesadelos",
                "Use luz de presença se tiver medo do escuro"
            ),
            challenges = listOf(
                "Transição do fim das sonecas",
                "Imaginação fértil = mais pesadelos",
                "Pode ter dificuldade para 'desligar' a mente",
                "Resistência ao dormir para 'não perder nada'"
            )
        )
    )

    val generalTips = listOf(
        SleepTip(
            title = "Ambiente Ideal para Dormir",
            icon = "🌙",
            tips = listOf(
                "Temperatura entre 20-22°C",
                "Quarto escuro (cortinas blackout)",
                "Ruído branco pode ajudar",
                "Berço/cama apenas para dormir",
                "Roupa de cama adequada à temperatura",
                "Sem telas no quarto"
            )
        ),
        SleepTip(
            title = "Sinais de Sono",
            icon = "😴",
            tips = listOf(
                "Bocejos frequentes",
                "Esfregar os olhos",
                "Olhar fixo ou vidrado",
                "Irritabilidade",
                "Perda de interesse em brinquedos",
                "Puxar as orelhas",
                "Ficar mais quieto"
            )
        ),
        SleepTip(
            title = "Rotina Noturna Ideal",
            icon = "🛁",
            tips = listOf(
                "Inicie 30-45 min antes de dormir",
                "Banho morno relaxante",
                "Massagem suave com óleo",
                "Troca de roupa/pijama",
                "Última mamada/alimentação",
                "História ou música calma",
                "Beijo de boa noite"
            )
        ),
        SleepTip(
            title = "Segurança no Sono",
            icon = "⚠️",
            tips = listOf(
                "Bebê sempre de barriga para cima",
                "Colchão firme e ajustado ao berço",
                "Nada solto no berço (cobertas, travesseiros, bichos)",
                "Não usar protetores de berço",
                "Temperatura adequada (não superaquecer)",
                "Berço no quarto dos pais até 6-12 meses"
            )
        )
    )

    data class SleepTip(
        val title: String,
        val icon: String,
        val tips: List<String>
    )

    // Regressões de sono conhecidas
    val sleepRegressions = listOf(
        SleepRegression(
            age = "4 meses",
            duration = "2-6 semanas",
            cause = "Mudança permanente nos ciclos de sono do bebê",
            description = "O bebê passa a ter ciclos de sono como adultos (leve e profundo). " +
                    "Acorda mais entre os ciclos e pode ter dificuldade para voltar a dormir sozinho.",
            tips = listOf(
                "Mantenha a rotina mesmo que pareça não funcionar",
                "Não crie novos hábitos (ex: ninar até dormir)",
                "Seja paciente - é uma fase de maturação",
                "Ofereça conforto sem exagerar nos estímulos",
                "Revise janelas de sono - podem precisar de ajuste"
            )
        ),
        SleepRegression(
            age = "8-10 meses",
            duration = "2-3 semanas",
            cause = "Marcos de desenvolvimento (engatinhar, ficar de pé, ansiedade de separação)",
            description = "O bebê está aprendendo muitas habilidades novas e quer praticar. " +
                    "A ansiedade de separação também aumenta nessa fase.",
            tips = listOf(
                "Permita prática das habilidades durante o dia",
                "Reforce vínculo durante o dia",
                "Mantenha despedidas curtas e seguras",
                "Ofereça objeto de transição (naninha)",
                "Seja consistente na rotina"
            )
        ),
        SleepRegression(
            age = "12 meses",
            duration = "1-2 semanas",
            cause = "Aniversário, primeiros passos, explosão de linguagem",
            description = "Muitas mudanças acontecendo! Pode resistir à soneca da manhã, " +
                    "mas ainda não está pronto para transição.",
            tips = listOf(
                "Não elimine a soneca da manhã ainda",
                "Pode ser transição falsa - aguarde",
                "Mantenha duas sonecas se possível",
                "Observe sinais de cansaço"
            )
        ),
        SleepRegression(
            age = "18 meses",
            duration = "2-6 semanas",
            cause = "Independência, dentes molares, ansiedade de separação intensa",
            description = "Uma das regressões mais desafiadoras. A criança está mais independente " +
                    "e pode resistir muito ao sono. Fase do 'não'.",
            tips = listOf(
                "Mantenha limites firmes e amorosos",
                "Ofereça escolhas ('Quer a luz azul ou amarela?')",
                "Seja consistente - não ceda aos pedidos extras",
                "Valide sentimentos, mas mantenha rotina",
                "Considere relógio de sono para hora de acordar"
            )
        ),
        SleepRegression(
            age = "2 anos",
            duration = "1-3 semanas",
            cause = "Mudanças de vida (novo irmão, transição para cama, desfralde)",
            description = "Pode coincidir com grandes mudanças. A criança está mais consciente " +
                    "e pode ter medos e pesadelos.",
            tips = listOf(
                "Evite muitas mudanças ao mesmo tempo",
                "Converse sobre medos durante o dia",
                "Use luz de presença se necessário",
                "Mantenha rotina previsível"
            )
        )
    )

    data class SleepRegression(
        val age: String,
        val duration: String,
        val cause: String,
        val description: String,
        val tips: List<String>
    )
}

