import React from "react"
import { Box } from '@mui/system';

function MenuBtn(props) {
    return (
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          width: "250px",
          height: "80px",
          marginTop: "20px",
          borderRadius: "20px",
          backgroundColor: "#FFFFFF",
          boxShadow: "5px 5px 8px rgba(0, 0, 0, 0.35)",
        }}>
        <h2>{props.name}</h2>
      </Box>
    )
}

export default MenuBtn;