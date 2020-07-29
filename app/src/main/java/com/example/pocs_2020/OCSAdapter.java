package com.example.pocs_2020;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pocs_2020.MiniOCS_Object.Medicine;
import com.example.pocs_2020.MiniOCS_Object.Patient;
import com.example.pocs_2020.MiniOCS_Object.Prescription;

import java.util.ArrayList;

/**
 * OCSAdapter 클래스 :: 프로젝트에서 사용되는 RecyclerView에 적용할 Adapter.
 * 여러 RecyclerView에서 사용하기 때문에 ViewType을 정의해  (getItemViewType메소드 정의)
 * 타입을 구분하여 각 타입에 맞게 ViewHolder를 정의한 뒤 사용하고 있음
 * 타입 구분은 Adapter를 생성할 때, 생성자 매개변수로 어느 Activity에서 사용하는 RecyclerView인지를 매개변수 activityName으로 받아
 * getItemViewType 메소드에서 activityName을 구분해 각 Activity마다 MiniOSCKey에서 정의한 Key번호를 부여,
 * onBindViewHolder에서 ViewType에 맞는 Viewholder에 데이터를 넣어주는 작업을 함.
 */
public class OCSAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // ViewHolder 크기 줄이기위해서 WIDTH,HEIGHT정의
    final static int WIDTH = 400;
    final static int HEIGHT = 80;

    Context mContext;
    String activityName;
    ArrayList<MiniOCSKey.RecyclerViewItem> dataList;
    MiniOCSKey.RecyclerViewItem data;
    //SparseBooleanArray : position별 선택 상태를 저장하는 구조
    SparseBooleanArray mSelectItems;

    /**
     * ViewHolder ItemView Onclick 리스너 인터페이스정의
     * -> RecyclerView에서 선택된 데이터가 무슨 데이터인지 Activity에 알려주기위해 Adapter에서 인터페이스를 정의한 뒤,
     * Activity 에서 인터페이스를 사용
     */
    public interface OnItemClickListener {
        void onItemClick(View v, int position, Patient patient);

        void onItemBarcodeCheck(boolean check);

        void onMedicineClick(View v, Medicine medicine);
    }

    OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public OCSAdapter(Context mContext, String activityName, ArrayList<MiniOCSKey.RecyclerViewItem> dataList) {
        this.mContext = mContext;
        this.activityName = activityName;
        this.dataList = dataList;
    }

    //ViewHolder 객체 정의
    public class PatientHolder_Consultation extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView sexText;
        TextView idText;
        TextView ageText;
        TextView deptText;

        public PatientHolder_Consultation(@NonNull final View itemView_consultation) {
            super(itemView_consultation);
            mSelectItems = new SparseBooleanArray(0);
            //consultation Activity에서 쓰이는 RecyclerView객체 정의
            idText = itemView_consultation.findViewById(R.id.consultation_item_id);
            nameText = itemView_consultation.findViewById(R.id.consultation_item_name);
            sexText = itemView_consultation.findViewById(R.id.consultation_item_sex);
            ageText = itemView_consultation.findViewById(R.id.consultation_item_age);
            deptText = itemView_consultation.findViewById(R.id.consultation_item_dept);
            if (activityName.equals(MiniOCSKey.PATIENTLIST_CONSULTATION)) {
                itemView_consultation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        // 선택되면 Vital 체크해서 저장
                        final int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            final Patient clickItem = (Patient) dataList.get(pos);
                            if (clickItem.getVital().getBP() == null || clickItem.getVital().getBT() == null ||
                                    clickItem.getVital().getHR() == null || clickItem.getVital().getRESP() == null) {
                                //Vital 정보가 없는 환자면 Dialog띄워서 Vital 저장먼저하기

                                CustomDialog customDialog = new CustomDialog(mContext);
                                customDialog.setupVitalDialog(clickItem);
                                customDialog.setOnSetupListener(new CustomDialog.OnSetupListener() {
                                    @Override
                                    public void onMedicinePrescription(String medicinePrescription) {
                                    }

                                    @Override
                                    public void onVitalCheck(Patient vitalPatient) {
                                        mListener.onItemClick(view, pos, vitalPatient);
                                        itemView_consultation.setBackground(mContext.getResources().getDrawable(R.drawable.strokebutton));
                                    }
                                });
                            } else {
                                //Vital 정보가 있는 환자인데, 선택이되면 하이라이팅 해주기
/*                                click += 1;
                                if (click % 2 != 0) {
                                    itemView.setBackgroundColor(Color.argb(50, 255, 204, 0));
                                    mListener.onItemBarcodeCheck(true);
                                } else
                                    itemView.setBackgroundColor(Color.WHITE);
                            */
                                if (mSelectItems.get(pos, false)) {
                                    //클릭 안 됐을 때
                                    mSelectItems.put(pos, false);
                                    itemView.setBackgroundColor(Color.WHITE);
                                } else {
                                    //클릭되었을 때
                                    mSelectItems.put(pos, true);
                                    itemView.setBackgroundColor(Color.argb(50, 255, 204, 0));
                                    mListener.onItemBarcodeCheck(true);
                                }
                            }
                        }
                    }
                });
            } else {
                //SURGERYACTIVITY 일때
                //Vital 정보가 있는 환자인데, 선택이되면 하이라이팅 해주기
                itemView_consultation.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            if (mSelectItems.get(pos, false)) {
                                mSelectItems.put(pos, false);
                                itemView.setBackground(mContext.getResources().getDrawable(R.drawable.strokebuttonwhite));

                            } else {
                                //클릭되면
                                mSelectItems.put(pos, true);
                                mListener.onItemClick(view, pos, (Patient) dataList.get(pos));
                                itemView.setBackgroundColor(Color.argb(50, 255, 204, 0));
                                mListener.onItemBarcodeCheck(true);

                            }
                        }
                    }
                });

            }

        }
    }

    public class PatientHolder_Pharmacy extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView sexText;
        TextView idText;
        TextView ageText;
        TextView medicineNameText;
        TextView medicinePrescriptionText;

        public PatientHolder_Pharmacy(@NonNull View itemView_pharmacy) {
            super(itemView_pharmacy);
            //PharmacyActivity에서 쓰이는 RecyclerView객체 정의

            idText = itemView_pharmacy.findViewById(R.id.pharmacy_item_id);
            nameText = itemView_pharmacy.findViewById(R.id.pharmacy_item_name);
            sexText = itemView_pharmacy.findViewById(R.id.pharmacy_item_sex);
            ageText = itemView_pharmacy.findViewById(R.id.pharmacy_item_age);
            medicinePrescriptionText = itemView_pharmacy.findViewById(R.id.pharmacy_item_medicinePrescription);
        }
    }

    public class PatientHolder_OldRecord extends RecyclerView.ViewHolder {
        TextView diseaseCodeText;
        TextView diseaseNameText;
        TextView DoctorMemoText;
        TextView DoctorOpinionText;
        TextView treatTimeText;

        public PatientHolder_OldRecord(@NonNull View itemView_oldrecord) {
            super(itemView_oldrecord);
            diseaseCodeText = itemView_oldrecord.findViewById(R.id.prescription_item_diseaseCode);
            diseaseNameText = itemView_oldrecord.findViewById(R.id.prescription_item_diseaseName);
            DoctorMemoText = itemView_oldrecord.findViewById(R.id.prescription_item_DoctorMemo);
            DoctorOpinionText = itemView_oldrecord.findViewById(R.id.prescription_item_DoctorOpnion);
            treatTimeText = itemView_oldrecord.findViewById(R.id.prescription_item_treatTime);
        }
    }

    public class MedicineHolder_Prescription extends RecyclerView.ViewHolder {
        TextView nameText;
        TextView infoText;
        TextView stockText;
        TextView typeText;

        public MedicineHolder_Prescription(@NonNull View itemView_medicine) {
            super(itemView_medicine);
            //Prescription Activity에서 쓰이는 RecyclerView객체 정의
            nameText = itemView_medicine.findViewById(R.id.medicine_item_name);
            infoText = itemView_medicine.findViewById(R.id.medicine_item_info);
            stockText = itemView_medicine.findViewById(R.id.medicine_item_stock);
            typeText = itemView_medicine.findViewById(R.id.medicine_item_type);
            itemView_medicine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    // 클릭하면 다이얼로그를 띄우고 다이얼로그에서 약처방한거 Text로 받아오기 -> Activity에서 약처방결과Text에 쓰일것->mListener에 등록
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        final Medicine clickItem = (Medicine) dataList.get(pos);
                        Log.e("click?", String.valueOf(clickItem));
                        CustomDialog customDialog = new CustomDialog(mContext);
                        customDialog.setupMedicineDialog(clickItem);
                        customDialog.setOnSetupListener(new CustomDialog.OnSetupListener() {
                            @Override
                            public void onMedicinePrescription(String medicinePrescription) {
                                clickItem.setMedicinePrescription(medicinePrescription);
                                mListener.onMedicineClick(view, clickItem);
                            }

                            @Override
                            public void onVitalCheck(Patient vitalPatient) {
                            }
                        });
                    }
                }
            });
        }
    }

    public class PharmacyHolder_Medicine_Prescription extends RecyclerView.ViewHolder {
        TextView medicineName;
        EditText tabletText, divideText, dayText;

        public PharmacyHolder_Medicine_Prescription(@NonNull View itemView_oldrecord) {
            super(itemView_oldrecord);
            medicineName = itemView_oldrecord.findViewById(R.id.pharmacy_prescription_item_medicineName);
            tabletText = itemView_oldrecord.findViewById(R.id.pharmacy_prescription_item_tablet);
            divideText = itemView_oldrecord.findViewById(R.id.pharmacy_prescription_item_divided);
            dayText = itemView_oldrecord.findViewById(R.id.pharmacy_prescription_item_day);

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (activityName.equals(MiniOCSKey.PATIENTLIST_CONSULTATION) || activityName.equals(MiniOCSKey.SURGERY)) {
            return MiniOCSKey.PATIENTLIST_CONSULTATION_KEY;
        } else if (activityName.equals(MiniOCSKey.PATIENTLIST_PRESCRIPTION)) {
            return MiniOCSKey.PATIENTLIST_PRESCRIPTION_KEY;
        } else if (activityName.equals(MiniOCSKey.PRESCRIPTION)) {
            // item_prescription
            return MiniOCSKey.PRESCRIPTION_KEY;
        } else if (activityName.equals(MiniOCSKey.PHARMACY)) {
            return MiniOCSKey.PHARMACY_KEY;
        } else if (activityName.equals(MiniOCSKey.MEDICINE)) {
            return MiniOCSKey.MEDICINE_KEY;
        } else if (activityName.equals(MiniOCSKey.MEDICNE_PRESCRIPTION)) {
            // PharmacyActivity에서 환자를 클릭하면 나오는 환자 약처방 다이얼로그 RecyclerView에 사용되는 ViewHolder
            return MiniOCSKey.MEDICINE_PRESCRIPTION_KEY;
        } else {
            return MiniOCSKey.OLDRECORD_KEY;
        }

    }

    @NonNull
    @Override
    //onCreateViewHolder : 아이템 뷰를 위한 뷰 홀더 객체 생성해서 리턴
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case MiniOCSKey.PATIENTLIST_CONSULTATION_KEY:
            case MiniOCSKey.PATIENTLIST_PRESCRIPTION_KEY:
                view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consultation, parent, false));
                return new PatientHolder_Consultation(view);
            case MiniOCSKey.PHARMACY_KEY:
                view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pharmacy, parent, false));
                return new PatientHolder_Pharmacy(view);
            case MiniOCSKey.MEDICINE_KEY:
                view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_medicine, parent, false));
                return new MedicineHolder_Prescription(view);
            case MiniOCSKey.OLDRECORD_KEY:
                view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_prescription, parent, false));
                return new PatientHolder_OldRecord(view);
            case MiniOCSKey.MEDICINE_PRESCRIPTION_KEY:
                view = (LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pharmacy_prescription, parent, false));
                return new PharmacyHolder_Medicine_Prescription(view);
        }
        return null;
    }

    //onBindViewHolder : 리싸이클러뷰의 각 position에 해당하는 데이터를 뷰 홀더의 아이템뷰에 표시 (position마다 각각 데이터 넣어줌)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        data = dataList.get(position);
        if (data instanceof Patient) {
            Patient Patient_item = (Patient) data;
            // ViewHolder에 들어갈 데이터가 Patient 객체일때 ->ConsultationActivity,PharmacyActivity에서 사용 할 RecyclerView.
            if (activityName.equals(MiniOCSKey.PATIENTLIST_CONSULTATION) || activityName.equals(MiniOCSKey.PATIENTLIST_PRESCRIPTION) || activityName.equals(MiniOCSKey.SURGERY)) {
                final PatientHolder_Consultation WaitListHolder = (PatientHolder_Consultation) holder;
                WaitListHolder.nameText.setText(Patient_item.getPatient_name());
                WaitListHolder.idText.setText(Patient_item.getPatient_id() + "");
                WaitListHolder.sexText.setText(Patient_item.getPatient_sex());
                WaitListHolder.ageText.setText(Patient_item.getPatient_age());
                WaitListHolder.deptText.setText(Patient_item.getPrescription().getDept());
                if (activityName.equals(MiniOCSKey.PATIENTLIST_PRESCRIPTION)) {
                    // ViewHolder 크기 조정
                    WaitListHolder.itemView.getLayoutParams().height = HEIGHT;
                    WaitListHolder.itemView.getLayoutParams().width = WIDTH;
                    WaitListHolder.itemView.setBackground(mContext.getDrawable(R.drawable.strokebutton));
                    WaitListHolder.idText.setTextSize(18);
                    WaitListHolder.idText.setWidth(30);
                    WaitListHolder.idText.setPadding(0, 10, 0, 10);
                    WaitListHolder.nameText.setTextSize(18);
                    WaitListHolder.nameText.setWidth(70);
                    WaitListHolder.nameText.setPadding(0, 10, 0, 10);
                    WaitListHolder.sexText.setTextSize(15);
                    WaitListHolder.sexText.setWidth(30);
                    WaitListHolder.sexText.setPadding(0, 10, 0, 10);
                    WaitListHolder.ageText.setTextSize(15);
                    WaitListHolder.ageText.setWidth(50);
                    WaitListHolder.ageText.setPadding(0, 10, 0, 10);
                    WaitListHolder.deptText.setTextSize(15);
                    WaitListHolder.deptText.setWidth(150);
                    WaitListHolder.deptText.setPadding(0, 10, 0, 10);

                } else if (activityName.equals(MiniOCSKey.PHARMACY)) {

                    PatientHolder_Pharmacy PharmacyHolder = (PatientHolder_Pharmacy) holder;
                    PharmacyHolder.nameText.setText(Patient_item.getPatient_name());
                    PharmacyHolder.idText.setText(Patient_item.getPatient_id() + "");
                    PharmacyHolder.sexText.setText(Patient_item.getPatient_sex());
                    PharmacyHolder.ageText.setText(Patient_item.getPatient_age());
                    PharmacyHolder.medicineNameText.setText(Patient_item.getPrescription().getMedicine().getMedicine_name());
                    PharmacyHolder.medicinePrescriptionText.setText(Patient_item.getPrescription().getPrescription_opinion());
                } else if (data instanceof Medicine) {
                    Medicine Medicine_item = (Medicine) data;
                    // ViewHolder에 들어갈 데이터가 Medicine 객체일때 -> PrescriptionActivity에서 사용 할 RecyclerView
                    if (activityName.equals(MiniOCSKey.MEDICINE)) {
                        MedicineHolder_Prescription MedicineHolder = (MedicineHolder_Prescription) holder;
                        MedicineHolder.nameText.setText(Medicine_item.getMedicine_name());
                        MedicineHolder.infoText.setText(Medicine_item.getMedicine_composition()); // 약 설명Text엔 약 구성성분(Medicine_composirion)을 넣음
                        MedicineHolder.stockText.setText(String.valueOf(Medicine_item.getStock_count()));
                        MedicineHolder.typeText.setText(Medicine_item.getMedicine_type());
                    }
                } else if (data instanceof Prescription) {
                    Prescription Prescription_item = (Prescription) data;
                    if (activityName.equals(MiniOCSKey.OLD_RECORD)) {
                        PatientHolder_OldRecord OldRecordHolder = (PatientHolder_OldRecord) holder;
                        OldRecordHolder.diseaseCodeText.setText(String.valueOf(Prescription_item.getDisease().getDisease_id()));
                        OldRecordHolder.diseaseNameText.setText(Prescription_item.getDisease().getDisease_name());
                        OldRecordHolder.treatTimeText.setText(Prescription_item.getTreat_time());
                        OldRecordHolder.DoctorOpinionText.setText(Prescription_item.getPrescription_opinion());
                        OldRecordHolder.DoctorMemoText.setText(Prescription_item.getPrescription_memo());
                        // 크기 조정
                        OldRecordHolder.itemView.getLayoutParams().height = HEIGHT;
                        OldRecordHolder.itemView.getLayoutParams().width = WIDTH;
                        OldRecordHolder.diseaseCodeText.setTextSize(18);
                        OldRecordHolder.diseaseCodeText.setWidth(15);
                        OldRecordHolder.diseaseCodeText.setPadding(0, 10, 0, 10);
                        OldRecordHolder.diseaseNameText.setTextSize(18);
                        OldRecordHolder.diseaseNameText.setWidth(70);
                        OldRecordHolder.diseaseNameText.setPadding(0, 10, 0, 10);
                        OldRecordHolder.DoctorMemoText.setTextSize(15);
                        OldRecordHolder.DoctorMemoText.setWidth(100);
                        OldRecordHolder.DoctorMemoText.setPadding(0, 10, 0, 10);
                        OldRecordHolder.DoctorOpinionText.setTextSize(15);
                        OldRecordHolder.DoctorOpinionText.setWidth(100);
                        OldRecordHolder.DoctorOpinionText.setPadding(0, 10, 0, 10);
                        OldRecordHolder.treatTimeText.setTextSize(15);
                        OldRecordHolder.treatTimeText.setWidth(70);
                        OldRecordHolder.treatTimeText.setPadding(0, 10, 0, 10);
                    }
                    else if (activityName.equals(MiniOCSKey.MEDICNE_PRESCRIPTION)) {
                        PharmacyHolder_Medicine_Prescription MedicinePrescriptionHolder = (PharmacyHolder_Medicine_Prescription) holder;
                        Log.e("OCSAdapter의 6",String.valueOf(Prescription_item));
                        MedicinePrescriptionHolder.medicineName.setText(Prescription_item.getMedicine().getMedicine_name());
                        MedicinePrescriptionHolder.tabletText.setText(Prescription_item.getMedicine_tablet());
                        MedicinePrescriptionHolder.divideText.setText(Prescription_item.getMedicine_divided());
                        MedicinePrescriptionHolder.dayText.setText(Prescription_item.getMedicine_day());
                    }
                }
            }
        }
    }

    //새로운 데이터로 전환
    public void upDateDateList(ArrayList<MiniOCSKey.RecyclerViewItem> newData) {
        this.dataList = newData;
        notifyDataSetChanged();
    }

    //데이터 하나 추가
    public void addItem(Patient newPatient) {
        dataList.add(newPatient);
    }

    //데이터 전체 삭제
    public void clear() {
        dataList.clear();
    }

    //getItemCount : 전체 데이터 갯수 리턴
    @Override
    public int getItemCount() {
        return dataList.size();
    }

}
