import Button from "@mui/material/Button";

function OAuth2FacebookSignInButton() {
    const facebookAuthorizationUrl = `${import.meta.env.VITE_OAUTH2_AUTHORIZATION_URL}/facebook`;
    return (
        <Button
            type="button"
            href={facebookAuthorizationUrl}
            fullWidth
            variant="contained"
            sx={{mt: 3, mb: 2}}
        >
            Sign In with Facebook
        </Button>
    );
}

export default OAuth2FacebookSignInButton;
