import {createTheme, ThemeProvider} from "@mui/material/styles";
import {ReactNode} from "react";
import {CssBaseline} from "@mui/material";
import useThemePaletteMode from "./ThemePaletteModeProvider.tsx";
import {lightPalette, darkPalette} from "../../config/colorPaletteConfig.ts";

interface AppThemeProviderProps {
  children: ReactNode;
}

export function AppThemeProvider({children}: AppThemeProviderProps) {
  const paletteMode = useThemePaletteMode().paletteMode;
  const theme = createTheme({
    palette: paletteMode === "light"
      ? lightPalette
      : darkPalette,
    components: {
      MuiCssBaseline: {
        styleOverrides: {
          body: {
            backgroundImage: "url(background.svg)",
            backgroundRepeat: "no-repeat",
            backgroundSize: "cover"
          }
        }
      },
      MuiButton: {
        defaultProps: {
          color: "inherit"
        }
      },
      MuiAlert: {
        styleOverrides: {
          standardInfo: {color: "secondary"},
          standardSuccess: {color: "success"},
          standardWarning: {color: "warning"},
          standardError: {color: "error"}
        },
        defaultProps: {variant: "standard"}
      }
    }
  });

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline/>
      {children}
    </ThemeProvider>
  );
}
