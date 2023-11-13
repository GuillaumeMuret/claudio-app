package com.niji.claudio.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.random.Random


@Serializable
data class Slack(
    val text: String? = null,
    val blocks: MutableList<SlackBlock> = mutableListOf(SlackBlock(text = SlackText(text = text)))
)

@Serializable
data class SlackBlock(
    val type: String = "section",
    @SerialName("block_id")
    val blockId: String = "section1",
    val accessory: SlackAccessory = SlackAccessory(),
    val text: SlackText? = null
)

@Serializable
data class SlackText(
    val text: String?,
    val type: String = "mrkdwn",
)

@Serializable
data class SlackAccessory(
    val type: String = "image",
    @SerialName("alt_text")
    val altText: String = "Gif de fou",
    @SerialName("image_url")
    val imageUrl: String = RandomGif.get()
)

object RandomGif {
    private val GIF_LIST: MutableList<String> = mutableListOf(
        "https://c.tenor.com/k0n-EAWb564AAAAM/simpsons-dr-zeus.gif",
        "https://c.tenor.com/p_z3iUfsXDYAAAAC/celebration-loop.gif",
        "https://c.tenor.com/RdepuTw_kK0AAAAC/happy-dancing.gif",
        "https://c.tenor.com/pXnGfrFQgF8AAAAC/dance-emoji.gif",
        "https://c.tenor.com/1XBEd972B2sAAAAd/dance-kid.gif",
        "https://c.tenor.com/jYlYXwyjkV4AAAAC/snoop-dogg-dancing.gif",
        "https://c.tenor.com/_zVoMVtS7IYAAAAi/oh-yeah.gif",
        "https://c.tenor.com/fyrcANGNGFUAAAAi/bunny-dancing-oh-yeah.gif",
        "https://c.tenor.com/ecWVLrNENfsAAAAC/rabbit-secret-life-of-pets.gif",
        "https://c.tenor.com/gxBpltSxt8IAAAAC/rabbit-happy.gif",
    )

    fun get(): String = GIF_LIST[Random.nextInt(GIF_LIST.size)]
}
