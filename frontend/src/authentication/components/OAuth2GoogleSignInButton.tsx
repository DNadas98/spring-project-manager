import {Button} from "@mui/material";

function OAuth2GoogleSignInButton() {
    const googleAuthorizationUrl = `${import.meta.env.VITE_OAUTH2_AUTHORIZATION_URL}/google`;
    return (
        < Button
            type="button"
            href={googleAuthorizationUrl}
            fullWidth
            variant="contained"
        >
            Sign In with Google
        </Button>
    );
}

export default OAuth2GoogleSignInButton;
