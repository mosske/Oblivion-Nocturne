@Composable
fun BrandRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource(Res.drawable.oblivion_owl),
            contentDescription = "Oblivion Nocturne Logo",
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp)),
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Oblivion Nocturne",
            style = typo().bodyMedium,
        )
    }
}
