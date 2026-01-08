package com.gestantes.checklist.util

import com.gestantes.checklist.data.preferences.CompanionData
import com.gestantes.checklist.data.preferences.CompanionSupportType

/**
 * ============================================================
 * SISTEMA DE MENSAGENS PERSONALIZADAS COM ACOMPANHANTE
 * ============================================================
 * 
 * ADITIVO - Não altera nenhuma lógica existente
 * Apenas fornece textos personalizados baseados no acompanhante
 * 
 * Se não houver acompanhante cadastrado, usa textos genéricos
 */
object CompanionMessages {
    
    // ============ MENSAGENS DE APOIO EMOCIONAL ============
    
    /**
     * Mensagem de apoio quando a gestante pode estar se sentindo sozinha
     */
    fun getYouAreNotAloneMessage(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Você não está sozinha. ${companion.name} está com você nessa jornada. 💗"
        } else {
            "Você não está sozinha. Sua rede de apoio está com você. 💗"
        }
    }
    
    /**
     * Sugestão para conversar sobre a semana
     */
    fun getTalkAboutWeekMessage(companion: CompanionData, week: Int): String {
        return if (companion.hasCompanion) {
            "Que tal conversar com ${companion.name} sobre essa semana $week? 💬"
        } else {
            "Que tal conversar com alguém de confiança sobre essa semana? 💬"
        }
    }
    
    /**
     * Mensagem de suporte emocional
     */
    fun getEmotionalSupportMessage(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Talvez ${companion.name} possa te apoiar nesse momento. 🤗"
        } else {
            "Talvez sua rede de apoio possa te ajudar nesse momento. 🤗"
        }
    }
    
    // ============ MENSAGENS PARA ORGANIZAÇÃO ============
    
    /**
     * Sugestão para organizar junto
     */
    fun getOrganizeTogetherMessage(companion: CompanionData, task: String): String {
        return if (companion.hasCompanion) {
            "Que tal organizar \"$task\" junto com ${companion.name}? 📋"
        } else {
            "Que tal pedir ajuda para organizar \"$task\"? 📋"
        }
    }
    
    /**
     * Mensagem sobre planejamento
     */
    fun getPlanningMessage(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Vocês podem planejar isso juntos! Converse com ${companion.name}. 📅"
        } else {
            "Vocês podem planejar isso juntos com quem te acompanha. 📅"
        }
    }
    
    // ============ MENSAGENS PARA CONSULTAS ============
    
    /**
     * Lembrete sobre consulta
     */
    fun getAppointmentReminderMessage(companion: CompanionData): String {
        return if (companion.hasCompanion && companion.supportTypes.contains(CompanionSupportType.APPOINTMENTS)) {
            "Lembre de avisar ${companion.name} sobre a consulta! 🏥"
        } else if (companion.hasCompanion) {
            "Se quiser companhia, convide ${companion.name} para a consulta. 🏥"
        } else {
            "Se quiser companhia, convide alguém de confiança. 🏥"
        }
    }
    
    // ============ MENSAGENS PARA PÓS-PARTO ============
    
    /**
     * Mensagem sobre preparação para o pós-parto
     */
    fun getPostpartumPrepMessage(companion: CompanionData): String {
        return if (companion.hasCompanion && companion.supportTypes.contains(CompanionSupportType.POSTPARTUM)) {
            "${companion.name} estará com você no pós-parto. Conversem sobre expectativas! 🤱"
        } else if (companion.hasCompanion) {
            "Converse com ${companion.name} sobre o pós-parto. 🤱"
        } else {
            "É importante ter apoio no pós-parto. Converse com sua rede de apoio. 🤱"
        }
    }
    
    // ============ MENSAGENS GENÉRICAS INCLUSIVAS ============
    
    /**
     * Referência inclusiva ao acompanhante
     */
    fun getInclusiveReference(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            companion.name
        } else {
            "quem te acompanha"
        }
    }
    
    /**
     * Mensagem sobre compartilhar sentimentos
     */
    fun getShareFeelingsMessage(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Compartilhe seus sentimentos com ${companion.name}. Faz bem! 💝"
        } else {
            "Compartilhe seus sentimentos com alguém de confiança. Faz bem! 💝"
        }
    }
    
    /**
     * Mensagem motivacional com acompanhante
     */
    fun getMotivationalMessage(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Você e ${companion.name} estão construindo algo lindo juntos! ✨"
        } else {
            "Você e sua rede de apoio estão construindo algo lindo! ✨"
        }
    }
    
    // ============ MENSAGENS PARA DIÁRIO ============
    
    /**
     * Texto para quando o momento envolve o acompanhante
     */
    fun getDiarySharedMomentText(companion: CompanionData): String {
        return if (companion.hasCompanion) {
            "Esse momento envolve ${companion.name} 💕"
        } else {
            "Esse momento envolve quem te acompanha 💕"
        }
    }
    
    // ============ MENSAGENS POR TIPO DE APOIO ============
    
    /**
     * Retorna uma mensagem personalizada baseada nos tipos de apoio
     */
    fun getMessageBySupportType(companion: CompanionData, type: CompanionSupportType): String {
        val name = companion.getDisplayName()
        
        return when (type) {
            CompanionSupportType.EMOTIONAL -> 
                "Conte com $name para apoio emocional. 💗"
            CompanionSupportType.PLANNING -> 
                "$name pode ajudar na organização! 📋"
            CompanionSupportType.APPOINTMENTS -> 
                "$name pode te acompanhar nas consultas. 🏥"
            CompanionSupportType.POSTPARTUM -> 
                "$name estará presente no pós-parto. 🤱"
        }
    }
    
    // ============ LISTA DE ITENS DE CHECKLIST SUGERIDOS ============
    
    /**
     * Novos itens de checklist relacionados ao acompanhante
     * ADITIVO - Apenas dados, não altera funcionamento dos checklists
     */
    fun getCompanionChecklistItems(companion: CompanionData): List<String> {
        val name = companion.getDisplayName("a pessoa que te acompanha")
        
        return listOf(
            "💬 Conversar com $name sobre essa fase da gestação",
            "📋 Organizar a lista do enxoval junto com $name",
            "💗 Compartilhar sentimentos dessa semana com $name",
            "🏥 Planejar quem te acompanhará nas próximas consultas",
            "🤱 Conversar sobre expectativas do pós-parto",
            "📅 Revisar o planejamento da chegada do bebê juntos",
            "💕 Agradecer $name pelo apoio nessa jornada"
        )
    }
}
