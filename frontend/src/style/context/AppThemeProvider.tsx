import {createTheme, ThemeProvider} from "@mui/material/styles";
import {ReactNode} from 'react';
import {CssBaseline} from "@mui/material";

interface AppThemeProviderProps {
  children: ReactNode;
}

export function AppThemeProvider({children}: AppThemeProviderProps) {
  const theme = createTheme({
    palette: {
      primary: {
        main: '#11508d',
      },
      secondary: {
        main: '#a4cfe1',
      }
    },
    typography: {
      h1: {
        fontSize: '3.5rem',
        fontWeight: 'bold',
      },
      h2: {
        fontSize: '2rem',
      },
      h3: {
        fontSize: '1.5rem',
      },
      body1: {
        color: "white",
      }
    }
  });

  return <ThemeProvider theme={theme}>
    <CssBaseline/>
    {children}
  </ThemeProvider>;
}
