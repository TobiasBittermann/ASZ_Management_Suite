import {Link} from "react-router-dom"
import houseImage from "../assets/ASZ_Freiburg_Haus.jpg"

function LoginPage() {
    return (
        <div className={"min-h-screen flex flex-col items-center justify-center bg-cover bg-center"}
        style={{backgroundImage:`url(${houseImage})`}}>
            <div className={"bg-white/50 backdrop-blur-sm shadow-lg rounded-2xl p-6 md:p-10 flex flex-col items-center gap-6 w-full max-w-4xl"}>

                <h1 className={"text-2xl md:text-3xl font-bold text-gray-800 text-center"}>
                    AV! Albingia-Schwarzwald-Zaringia
                </h1>

                <p className={"text-gray-800 text-sm"}>
                    Bitte logge dich ein um alle Funktionen
                </p>
                <p>
                    <Link to="/member" className="w-full flex justify-center">
                        <button
                            className={"w-full max-w-xs bg-green-300 hover:bg-green-500 text-black shadow-md rounded px-6 py-2 transition hover:scale-105"}>
                            Login
                        </button>
                    </Link>
                </p>
            </div>
        </div>

    );
}

export default LoginPage;