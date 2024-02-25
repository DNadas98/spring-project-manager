import {Button} from "@mui/material";
import useThemePaletteMode from "../context/ThemePaletteModeProvider.tsx";
import {DarkMode, LightMode} from "@mui/icons-material";

export default function ThemePaletteModeSwitch() {
  const {paletteMode, togglePaletteMode} = useThemePaletteMode();
  return (
    <Button color={"inherit"} onClick={togglePaletteMode}>
      {paletteMode === "light" ? <LightMode/> : <DarkMode/>}
    </Button>
  );
}
