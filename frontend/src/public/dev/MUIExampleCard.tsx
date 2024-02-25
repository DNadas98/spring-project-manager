import {Button, Card, CardActions, CardContent, CardMedia, Typography} from "@mui/material";

export default function MUIExampleCard() {
    return (
        <Card>
            <CardMedia
                component="img"
                alt="Image"
                height="140"
                image="/vite.svg"
                sx={{objectFit:"contain"}}
            />
            <CardContent>
                <Typography color="textSecondary"  gutterBottom variant="h5" component="h2">
                    Card Example
                </Typography>
                <Typography variant="body2" component="p">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac
                    magna libero. Proin vel lacus vitae massa euismod tincidunt eget ut
                    dui. Sed id sem a risus fermentum eleifend congue vitae sem. Donec
                    faucibus lorem
                    id eros scelerisque congue. Praesent vehicula libero vitae purus
                    mollis
                    molestie ac at lectus.
                </Typography>
            </CardContent>
            <CardActions>
                <Button size="small" color="secondary">
                    View
                </Button>
                <Button size="small" color="secondary">
                    Edit
                </Button>
            </CardActions>
        </Card>);
}
