import {useNavigate, useSearchParams} from "react-router-dom";
import {useState} from "react";
import houseImage from "../assets/ASZ_Freiburg_Haus.jpg";

function RegisterPage() {
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");
    const navigate = useNavigate();
    const [error, setError] = useState<string | null>(null);

    async function register() {
        setError(null);
        try {
            const startResponse = await fetch("/webauthn/register/start", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({token})
            })
            if (!startResponse.ok) {
                throw new Error("Einladung ungültig oder abgelaufen.")
            }

            const {options} = await startResponse.json();
            const publicKey = PublicKeyCredential.parseCreationOptionsFromJSON(options.publicKey);
            const credential = await navigator.credentials.create({publicKey}) as PublicKeyCredential;

            const finishResponse = await fetch("/webauthn/register/finish", {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({token, response: JSON.stringify(credential.toJSON())})
            });
            if (!finishResponse.ok) {
                throw new Error("Registrierung fehlgeschlagen.")
            }

            navigate("/")
        } catch (e) {
            if (e instanceof Error) {
                setError(e.name === "NotAllowedError" ? "Vorgang abgebrochen." : e.message);
            }
        }

        return (
            <div className={"min-h-screen flex flex-col items-center justify-center bg-cover bg-center"}
                 style={{backgroundImage: `url(${houseImage})`}}>
                <div
                    className={"bg-white/50 backdrop-blur-sm shadow-lg rounded-2xl p-6 md:p-10 flex flex-col items-center gap-6 w-full max-w-4xl"}>
                    <h1 className={"text-2xl md:text-3xl font-bold text-gray-800 text-center"}>
                        Passkey einrichten
                    </h1>
                    {!token ? (
                            <p className={"text-red-700"}>Kein Einladungslink gefunden.</p>)
                        : (
                            <button onClick={register}
                                    className={"w-full max-w-xs bg-green-300 hover:bg-green-500 text-black shadow-md rounded px-6 py-2 transition herver:scale-105"}>
                                Passkey erstellen
                            </button>
                        )}
                    {error && <p className={"text-red-700"}>{error}</p>}
                </div>
            </div>
        );
    }
}

export default RegisterPage;