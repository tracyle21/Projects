using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class Notifications : MonoBehaviour
{
    public Image notification;
    public Text message;
    public string newMessage;
    private bool show;
    // Start is called before the first frame update
    void Start()
    {
        //notification = gameObject.GetComponent<Image>();
        //message = gameObject.GetComponentInChildren<Text>();
        show = false;
        notification.transform.localPosition = new Vector3(0, 2100, 0);
    }

    // Update is called once per frame
    void Update()
    {
        
    }


    public void display()
    {
        show = !show;
        if(show)
        {
            notification.transform.localPosition = new Vector3(0, 0, 0);
            changeMessage();
        }
        else
        {
            notification.transform.localPosition = new Vector3(0, 2100, 0);
        }
    }

    void changeMessage()
    {
        message.text = newMessage;
    }
}
