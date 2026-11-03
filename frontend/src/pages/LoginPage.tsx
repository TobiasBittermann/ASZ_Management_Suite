import {Link, useNavigate} from "react-router-dom"
import houseImage from "../assets/ASZ_Freiburg_Haus.jpg"
import {useState} from "react";

function LoginPage() {
    const navigate = useNavigate();
    const [error, setError] = useState<string | null>(null);

    async function login() {
        setError(null);
        try{
            const startResponse = await fetch ("/webauthn/login/start", {
                method:"POST"
            });
            if(!startResponse.ok){
                throw new Error("Login konnte nicht gestartet werden.");
            }

            const {requestId, options} = await startResponse.json();
            const publicKey = PublicKeyCredential.parseRequestOptionsFromJSON(options.publicKey);
            const credential = await navigator.credentials.get({publicKey}) as PublicKeyCredential;

            const finishResponse = await fetch ("/webauthn/login/finish", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({requestId, response: JSON.stringify(credential.toJSON())})
            });
            if(!finishResponse.ok){
                throw new Error("Login fehlgeschlagen.");
            }

            navigate("/home");
        }
        catch (e) {
            if (e instanceof Error){
                setError(e.name === "NotAllowedError" ? "Vorgang abgebrochen." : e.message );
            }
        }
    }


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
                        <button onClick={login}
                            className={"w-full max-w-xs bg-green-300 hover:bg-green-500 text-black shadow-md rounded px-6 py-2 transition hover:scale-105"}>
                            Login
                        </button>

                        {error && <p className={"text-red-700"}>{error}</p>}
            </div>
        </div>

    );
}

export default LoginPage;