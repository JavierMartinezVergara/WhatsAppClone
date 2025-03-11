package com.example.conversations.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.conversations.ui.model.Conversation
import com.example.framework.ui.Avatar

@Composable
fun ConversationItem(conversation: Conversation) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Avatar(
            imageUrl = conversation.avatar,
            size = 50.dp,
            contentDescription = "${conversation.name}´s avatar"
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = conversation.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
            Text(
                text = conversation.message,
                maxLines = 1,
                softWrap = true,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                text = conversation.timestamp,
                textAlign = TextAlign.End
            )
            if (conversation.unreadCount > 0) {
                val colorBackground = MaterialTheme.colorScheme.onSurface
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = conversation.unreadCount.toString(),
                    fontWeight = FontWeight.Bold,
                    color = colorBackground,
                    modifier = Modifier.width(IntrinsicSize.Min)
                        .drawBehind {
                            drawCircle(
                                color = colorBackground,
                                radius = this.size.minDimension
                            )
                        },
                )
            }
        }


    }
}