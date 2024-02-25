import Button from "@mui/material/Button";

function OAuth2GithubSignInButton() {
    const githubAuthorizationUrl = `${import.meta.env.VITE_OAUTH2_AUTHORIZATION_URL}/github`;
    return (
        <Button
            type="button"
            href={githubAuthorizationUrl}
            fullWidth
            variant="contained"
            sx={{mt: 3, mb: 2}}
        >
            Sign In with GitHub
        </Button>
    );
}

export default OAuth2GithubSignInButton;