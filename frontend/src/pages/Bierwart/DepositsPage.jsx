import {useEffect, useState} from "react";
import {FiEdit3, FiPlusCircle, FiTrash2} from "react-icons/fi";
import {Tooltip} from "react-tooltip";
import BwDepositAddEdit from "../../components/bierwart/BwDepositAddEdit.jsx";
import {getAccountTypeLable, getMemberName} from "../../utils/namingUtils.jsx";
import {loadAccountTypes, loadBwDeposits, loadMembers} from "../../utils/loadUtils.jsx";
import {deleteEntity, saveEntity} from "../../utils/crudUtils.jsx";

function BwDepositsTab() {
    const [bwDeposits, setBwDeposit] = useState([]);
    const [members, setMembers] = useState([]);
    const [accountTypes, setAccountTypes] = useState([])
    const [selectedBwDeposit, setSelectedBwDeposit] = useState(null);
    const [isModalOpen, setIsModalOpen] = useState(false);

    useEffect(() => {
        loadBwDeposits(setBwDeposit);
        loadMembers(setMembers);
        loadAccountTypes(setAccountTypes)
    }, [])

    async function handleSaveBwDeposit(deposit){
        await saveEntity(deposit, "/bwdeposits", loadBwDeposits, setBwDeposit)
    }

    async function handleDeleteBwDeposit(id){
        await deleteEntity(id, "/bwdeposits", loadBwDeposits, setBwDeposit)
    }

    function handleEditClick(deposit) {
        setSelectedBwDeposit(deposit);
        setIsModalOpen(true);
    }

    function handleAddClick() {
        setSelectedBwDeposit(null);
        setIsModalOpen(true);
    }

    return (
        <div>
            <h3 className={"text-3xl font-bold text-gray-800 text-center"}>
                Einzahlungen
            </h3>

            <button
                className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md justify-self-start rounded px-6 py-2 m-3 transition"}
                data-tooltip-id={"add-tip"}
                data-tooltip-content={"Add a new deposit"}
                onClick={handleAddClick}>
                <FiPlusCircle/>
            </button>
            <Tooltip id={"add-tip"}/>

            {
                isModalOpen && (
                    <BwDepositAddEdit
                        bwDeposit={selectedBwDeposit}
                        members={members}
                        accountTypes={accountTypes}
                        onClose={() => setIsModalOpen(false)}
                        onSave={handleSaveBwDeposit}/>
                )}

            <div className={"overflow-x-auto rounded-xl shadow"}>

                <table className={"min-w-full bg-white text-sm text-left"}>
                    <thead className={"bg-gray-200 text-gray-600 uppercase text-xs"}>
                    <tr>
                        <th className={"px-6 py-3"}>Id</th>
                        <th className={"px-6 py-3"}>Mitglied</th>
                        <th className={"px-6 py-3"}>Einzahlung in €</th>
                        <th className={"px-6 py-3"}>Einzahlung auf</th>
                        <th className={"px-6 py-3"}>Buchungsdatum</th>
                        <th className={"px-6 py-3"}>Aktionen</th>
                    </tr>
                    </thead>
                    <tbody className={"divide-y divide-gray-100"}>
                    {bwDeposits.map(deposit => (
                        <tr key={deposit.id} className={"hover:bg-gray-50 transition"}>
                            <td className={"px-6 py-3"}>{deposit.id}</td>
                            <td className={"px-6 py-3"}>{getMemberName(members, deposit.memberId)}</td>
                            <td className={"px-6 py-3"}>{deposit.deposit}</td>
                            <td className={"px-6 py-3"}>{getAccountTypeLable(deposit.accountType)}</td>
                            <td className={"px-6 py-3"}>{deposit.depositDate}</td>
                            <td>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"edit-tip"}
                                    data-tooltip-content={"Edit a deposit entry"}
                                    onClick={() => handleEditClick(deposit)}>
                                    <FiEdit3/>
                                </button>
                                <button
                                    className={"hover:bg-green-500 hover:scale-105 bg-green-300 text-black shadow-md rounded px-3 py-1 m-1 transition"}
                                    data-tooltip-id={"delete-tip"}
                                    data-tooltip-content={"Delete a deposit entry"}
                                    onClick={() => handleDeleteBwDeposit(deposit.id)}>
                                    <FiTrash2/>
                                </button>
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>

                <Tooltip id={"edit-tip"}/>
                <Tooltip id={"delete-tip"}/>
            </div>

        </div>
    )
}

export default BwDepositsTab;