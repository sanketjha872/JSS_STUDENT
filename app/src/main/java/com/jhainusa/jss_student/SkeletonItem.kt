package com.jhainusa.jss_student

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.shimmer

@Composable
fun SkeletonYearItem() {

    val shimmer = PlaceholderHighlight.shimmer()

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(horizontal = 18.dp)
                .clip(RoundedCornerShape(18.dp))
                .background(Color(0xFFF5F5F5))
                .padding(horizontal = 18.dp, vertical = 20.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.Start,
            ) {

                // Title Skeleton (BTech 1st Year)
                Box(
                    modifier = Modifier
                        .height(16.dp)
                        .width(140.dp)
                        .placeholder(
                            visible = true,
                            highlight = shimmer
                        )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Subtitle Skeleton (Even & Odd Sem)
                Box(
                    modifier = Modifier
                        .height(12.dp)
                        .width(100.dp)
                        .placeholder(
                            visible = true,
                            highlight = shimmer
                        )
                )
            }

            // Arrow circle skeleton
            Box(
                modifier = Modifier
                    .padding(5.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .placeholder(
                        visible = true,
                        shape = CircleShape,
                        highlight = shimmer
                    )
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}