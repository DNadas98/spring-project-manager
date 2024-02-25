import React, {ChangeEvent} from "react";
import {
    Checkbox,
    FormControl,
    FormControlLabel,
    FormGroup,
    FormLabel,
    Grid,
    InputLabel,
    MenuItem,
    Radio,
    RadioGroup,
    Select,
    Slider,
    Switch,
    TextField,
    Typography
} from "@mui/material";
import {DateTimePicker, LocalizationProvider} from "@mui/x-date-pickers";
import {AdapterDateFns} from "@mui/x-date-pickers/AdapterDateFns";

export default function MUIExampleForm() {
    const [formData, setFormData] = React.useState({
        textField: "",
        select: "",
        checkbox: false,
        radioGroup: "",
        switch: false,
        slider: 30,
        dateTime: new Date()
    });

    const handleInputChange = (event: ChangeEvent<HTMLInputElement>) => {
        const {name, value, checked} = event.target as HTMLInputElement;
        setFormData((prevFormData) => ({
            ...prevFormData,
            [name!]: event.target?.type === "checkbox" ? checked : value
        }));
    };

    const handleSliderChange = (_event: ChangeEvent, newValue: number) => {
        setFormData((prevFormData) => ({
            ...prevFormData,
            slider: Array.isArray(newValue) ? newValue[0] : newValue
        }));
    };

    const handleDateTimeChange = (newValue: Date | null) => {
        setFormData((prevFormData) => ({
            ...prevFormData,
            dateTime: newValue ?? new Date() // Fallback to current date if `null`
        }));
    };

    return (
        <LocalizationProvider dateAdapter={AdapterDateFns}>
            <Grid container spacing={2}>
                <Grid item xs={12}>
                    <TextField
                        fullWidth
                        label="TextField"
                        variant="outlined"
                        name="textField"
                        value={formData.textField}
                        onChange={handleInputChange}
                    />
                </Grid>
                <Grid item xs={12}>
                    <FormControl fullWidth>
                        <InputLabel>Select</InputLabel>
                        <Select
                            fullWidth
                            label="Select"
                            name="select"
                            value={formData.select}
                            onChange={handleInputChange}
                        >
                            <MenuItem value={10}>Ten</MenuItem>
                            <MenuItem value={20}>Twenty</MenuItem>
                        </Select>
                    </FormControl>
                </Grid>
                <Grid item xs={12}>
                    <FormGroup>
                        <FormControlLabel
                            control={<Checkbox checked={formData.checkbox}
                                               onChange={handleInputChange}
                                               name="checkbox"/>} label="Checkbox"/>
                    </FormGroup>
                </Grid>
                <Grid item xs={12}>
                    <FormControl component="fieldset">
                        <FormLabel component="legend">RadioGroup</FormLabel>
                        <RadioGroup name="radioGroup"
                                    value={formData.radioGroup}
                                    onChange={handleInputChange} row>
                            <FormControlLabel value="option1" control={<Radio/>}
                                              label="Option 1"/>
                            <FormControlLabel value="option2" control={<Radio/>}
                                              label="Option 2"/>
                        </RadioGroup>
                    </FormControl>
                </Grid>
                <Grid item xs={12}>
                    <FormControlLabel
                        control={<Switch checked={formData.switch}
                                         onChange={handleInputChange}
                                         name="switch"/>} label="Switch"/>
                    <Grid item xs={12}>
                        <Typography gutterBottom>
                            Slider
                        </Typography>
                        <Slider name="slider" value={formData.slider}
                                onChange={handleSliderChange}/>
                    </Grid>
                </Grid>
                <Grid item xs={12}>
                    <Typography id={"datetime"} gutterBottom>
                        DateTimePicker
                    </Typography>
                    <DateTimePicker
                        name="datetime"
                        format="MM/dd/yyyy HH:mm"
                        value={formData.dateTime}
                        onChange={handleDateTimeChange}
                        slotProps={{textField: {fullWidth: true}}}
                    />
                </Grid>
            </Grid>
        </LocalizationProvider>
    );
}
