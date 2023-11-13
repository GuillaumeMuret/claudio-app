package com.niji.claudio.common.data.feature.media.usecase

import com.niji.claudio.common.data.model.Media
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetMockMedias {
    fun execute(): Flow<List<Media>> {
        return flow {
            emit(MEDIAS.sortedBy { it.title })
        }
    }

    companion object {
        val MEDIAS = listOf(
            Media(
                bddId = 0,
                filePath = "/un/super/dossier/media0.mp3",
                serverId = "serverId0",
                url = "url0",
                title = "Titre 1",
            ),
            Media(
                bddId = 1,
                filePath = "/un/super/dossier/media0.mp3",
                serverId = "serverId1",
                url = "url1",
                title = "Autre titre",
            ),
        )
    }
}