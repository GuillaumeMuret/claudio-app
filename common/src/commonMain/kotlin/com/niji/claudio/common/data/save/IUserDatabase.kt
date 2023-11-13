package com.niji.claudio.common.data.save

import com.niji.claudio.common.data.model.User

interface IUserDatabase {
    suspend fun getUser(): User
    suspend fun addUser(user: User): User
    suspend fun updateUser(user: User)
}