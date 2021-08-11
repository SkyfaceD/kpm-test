package org.skyfaced.kpm_test.repository.archive

import org.skyfaced.kpm_test.model.ArchiveInfo

interface ArchiveRepository {
    suspend fun fetchArchiveInfo(
        currencyPairs: String,
        from: Int,
        to: Int,
    ): List<ArchiveInfo>?
}