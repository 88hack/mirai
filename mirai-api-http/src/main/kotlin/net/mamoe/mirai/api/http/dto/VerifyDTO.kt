package net.mamoe.mirai.api.http.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import net.mamoe.mirai.api.http.AuthedSession

@Serializable
abstract class VerifyDTO : DTO {
    abstract val sessionKey: String
    @Transient
    lateinit var session: AuthedSession // 反序列化验证后传入
}

@Serializable
data class BindDTO(override val sessionKey: String, val qq: Long) : VerifyDTO()


// 写成data class并继承DTO接口是为了返回时的形式统一
@Serializable
sealed class StateCodeDTO(val code: Int, var msg: String) : DTO {
    object Success : StateCodeDTO(0, "success") // 成功
    // object AUTH_WRONG : CodeDTO(1) // AuthKey错误, @see AuthResDTO
    object NoBot : StateCodeDTO(2, "指定Bot不存在")
    object IllegalSession : StateCodeDTO(3, "Session失效或不存在")
    object NotVerifySession : StateCodeDTO(4, "Session未认证")
    object NoElement : StateCodeDTO(5, "指定对象不存在")

    // KS bug: 主构造器中不能有非字段参数 https://github.com/Kotlin/kotlinx.serialization/issues/575
    @Serializable
    class IllegalAccess() : StateCodeDTO(400, "") { // 非法访问
        constructor(msg: String) : this() {
            this.msg = msg
        }
    }
}

@Serializable
data class SendDTO(
    override val sessionKey: String,
    val target: Long,
    val messageChain: MessageChainDTO
) : VerifyDTO()
