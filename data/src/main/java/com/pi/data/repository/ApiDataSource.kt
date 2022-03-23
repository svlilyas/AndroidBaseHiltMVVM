package com.pi.data.repository

import com.pi.data.remote.ProjectService
import javax.inject.Inject

class ApiDataSource @Inject constructor(
    private val projectService: ProjectService
)
