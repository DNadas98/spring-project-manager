import {createContext, ReactNode, useContext, useEffect, useState} from "react";
import {PaletteMode} from "@mui/material";

interface AppThemePaletteProviderProps {
  children: ReactNode;
}

export interface IThemePaletteModeContext {
  paletteMode: PaletteMode;
  togglePaletteMode: () => void;
}

const ThemePaletteModeContext = createContext<IThemePaletteModeContext>({
  paletteMode: "light",
  togglePaletteMode: () => {
  }
});

export function ThemePaletteModeProvider({children}: AppThemePaletteProviderProps) {
  const [paletteMode, setPaletteMode] = useState<PaletteMode>(
    localStorage.getItem("paletteMode") as PaletteMode || "light"
  );

  useEffect(() => {
    const storedMode = localStorage.getItem("paletteMode") as PaletteMode;
    if (storedMode) {
      setPaletteMode(storedMode);
    }
  }, []);

  const togglePaletteMode = () => {
    setPaletteMode((previousMode) => {
      const newMode = previousMode === "light" ? "dark" : "light";
      localStorage.setItem("paletteMode", newMode);
      return newMode;
    });
  };

  return (
    <ThemePaletteModeContext.Provider value={{paletteMode, togglePaletteMode}}>
      {children}
    </ThemePaletteModeContext.Provider>);
}

export default function useThemePaletteMode(): IThemePaletteModeContext {
  return useContext(ThemePaletteModeContext);
}
