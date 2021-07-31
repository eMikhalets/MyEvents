package com.emikhalets.mydates.ui.app_components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.emikhalets.mydates.R
import com.emikhalets.mydates.ui.theme.AppTheme

@Composable
fun AppTextField(
    label: String,
    value: String,
    singleLine: Boolean,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    onClick: () -> Unit = {},
    requiredError: Boolean = false
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = AppTheme.colors.card,
        elevation = 4.dp,
        modifier = modifier.clickable { onClick() }
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = MaterialTheme.typography.body1,
            readOnly = imageVector != null,
            singleLine = singleLine,
            shape = RoundedCornerShape(16.dp),
            label = {
                Text(
                    text = label,
                    color = AppTheme.colors.onTextFieldHint
                )
            },
            colors = TextFieldDefaults.textFieldColors(
                textColor = AppTheme.colors.onTextField,
                backgroundColor = AppTheme.colors.textField,
                trailingIconColor = AppTheme.colors.onTextField,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            trailingIcon = {
                if (imageVector != null) Icon(
                    imageVector = imageVector,
                    contentDescription = ""
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
        if (requiredError) {
            Text(
                text = stringResource(R.string.empty_text_field),
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.End,
                color = Color.Red,
                modifier = Modifier
                    .padding(end = 16.dp, top = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppTextFieldPreview() {
    AppTheme {
        AppTextField("Preview", "Preview text field", false, {})
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, backgroundColor = 1)
@Composable
private fun AppTextFieldDarkPreview() {
    AppTheme {
        AppTextField("Preview", "Preview text field", false, {})
    }
}